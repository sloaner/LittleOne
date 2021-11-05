package com.jsloane.littleone.domain.usecases

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.domain.LOFirestore
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class JoinFamilyByInviteCodeUseCase @Inject constructor() :
    ResultUseCase<JoinFamilyByInviteCodeUseCase.Params, Boolean>() {
    override suspend fun doWork(params: Params): Boolean =
        withContext(AppCoroutineDispatchers.io) {
            val userId = Firebase.auth.currentUser?.uid

            if (userId.isNullOrBlank())
                throw FirebaseFirestoreException(
                    "UNAUTHORIZED",
                    FirebaseFirestoreException.Code.UNAUTHENTICATED
                )

            val inviteFamilyDoc = Firebase.firestore
                .collection(LOFirestore.Family.id)
                .whereEqualTo(
                    LOFirestore.Family.Field.inviteCode.name,
                    params.inviteCode
                )
                .whereGreaterThanOrEqualTo(
                    LOFirestore.Family.Field.inviteExpiration.name,
                    Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), 0)
                )
                .get()
                .await()

            if (inviteFamilyDoc.documents.size == 0)
                throw FirebaseFirestoreException(
                    "NOT_FOUND",
                    FirebaseFirestoreException.Code.NOT_FOUND
                )
            if (inviteFamilyDoc.documents.size > 1)
                throw FirebaseFirestoreException(
                    "INVITE_CODE_COLLISION",
                    FirebaseFirestoreException.Code.FAILED_PRECONDITION
                )

            inviteFamilyDoc.documents.first().reference.update(
                LOFirestore.Family.Field.users.name,
                FieldValue.arrayUnion(userId)
            ).await()

            return@withContext true
        }

    data class Params(val inviteCode: String) : UseCase.Params
}
