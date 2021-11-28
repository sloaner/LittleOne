package com.jsloane.littleone.domain.usecases

import com.google.firebase.firestore.FirebaseFirestoreException
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class JoinFamilyByInviteCodeUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : UseCase<JoinFamilyByInviteCodeUseCase.Params>() {
    override suspend fun doWork(params: Params) = withContext(AppCoroutineDispatchers.io) {
        if (params.userId.isNullOrBlank())
            throw FirebaseFirestoreException(
                "UNAUTHORIZED",
                FirebaseFirestoreException.Code.UNAUTHENTICATED
            )

        repository.findFamilyByInvite(params.inviteCode).collect {
            when (it) {
                is Result.Error ->
                    throw FirebaseFirestoreException(
                        "NOT_FOUND",
                        FirebaseFirestoreException.Code.NOT_FOUND
                    )
                is Result.Loading -> {}
                is Result.Success -> repository.updateFamilyUsers(it.data.id, params.userId)
            }
        }
    }

    data class Params(
        val userId: String?,
        val inviteCode: String
    ) : UseCase.Params
}
