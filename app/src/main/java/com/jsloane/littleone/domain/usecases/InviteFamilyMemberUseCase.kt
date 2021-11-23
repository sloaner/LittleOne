package com.jsloane.littleone.domain.usecases

import com.google.firebase.firestore.FirebaseFirestoreException
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.lastOrNull

class InviteFamilyMemberUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<InviteFamilyMemberUseCase.Params, Flow<Result<Unit>>>() {

    override suspend fun doWork(params: Params): Flow<Result<Unit>> {
        val inviteCode = findValidInviteCode()
        return repository.createFamilyInvite(
            familyId = params.family_id,
            inviteCode = inviteCode,
            inviteExpiration = LocalDateTime.now().plusDays(1L)
        )
    }

    private suspend fun findValidInviteCode(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        var i = 0

        do {
            val inviteCode = (1..6).map { allowedChars.random() }.joinToString("")
            val codeResults = repository.findFamilyByInvite(inviteCode).lastOrNull()

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
