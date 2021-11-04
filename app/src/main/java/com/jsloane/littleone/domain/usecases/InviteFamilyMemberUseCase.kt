package com.jsloane.littleone.domain.usecases

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.domain.FirestoreCollection
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class InviteFamilyMemberUseCase @Inject constructor() :
    ResultUseCase<InviteFamilyMemberUseCase.Params, Boolean>() {
    override suspend fun doWork(params: Params): Boolean =
        withContext(AppCoroutineDispatchers.io) {
            val inviteCode = findValidInviteCode()

            Firebase.firestore
                .collection(FirestoreCollection.Family.id)
                .document(params.family_id)
                .update(
                    mapOf(
                        FirestoreCollection.Family.Field.inviteCode.name to
                            inviteCode,
                        FirestoreCollection.Family.Field.inviteExpiration.name to
                            Timestamp(
                                LocalDateTime.now()
                                    .plusDays(1)
                                    .toEpochSecond(ZoneOffset.UTC),
                                0
                            )
                    )
                ).await()

            return@withContext true
        }

    private suspend fun findValidInviteCode(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        var i = 0

        do {
            val inviteCode = (1..6).map { allowedChars.random() }.joinToString("")
            val codeResults = Firebase.firestore
                .collection(FirestoreCollection.Family.id)
                .whereEqualTo(
                    FirestoreCollection.Family.Field.inviteCode.name,
                    inviteCode
                )
                .whereGreaterThanOrEqualTo(
                    FirestoreCollection.Family.Field.inviteExpiration.name,
                    Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), 0)
                )
                .get()
                .await()
            if (codeResults.isEmpty)
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
