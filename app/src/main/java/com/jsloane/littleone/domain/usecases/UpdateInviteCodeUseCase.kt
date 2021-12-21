package com.jsloane.littleone.domain.usecases

import com.google.firebase.firestore.FirebaseFirestoreException
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last

class UpdateInviteCodeUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : UseCase<UpdateInviteCodeUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        val inviteCode = findValidInviteCode()
        repository.createInviteCode(
            familyId = params.family_id,
            inviteCode = inviteCode,
            inviteExpiration = Instant.now().plus(1L, ChronoUnit.DAYS)
        ).last()
    }

    private suspend fun findValidInviteCode(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        var i = 0

        do {
            val inviteCode = (1..6).map { allowedChars.random() }.joinToString("")
            val codeResults = repository.findFamilyByInvite(inviteCode)
                .filterIsInstance<Result.Success<Family>>()
                .firstOrNull()

            if (codeResults == null)
                return inviteCode
            else
                i += 1
        } while (i < 5)

        throw FirebaseFirestoreException("INVALID_CODE", FirebaseFirestoreException.Code.CANCELLED)
    }

    data class Params(
        val family_id: String
    ) : UseCase.Params
}
