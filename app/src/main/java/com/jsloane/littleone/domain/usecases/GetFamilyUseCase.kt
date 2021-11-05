package com.jsloane.littleone.domain.usecases

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.data.entities.Family
import com.jsloane.littleone.domain.FirestoreCollection
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GetFamilyUseCase @Inject constructor() : ResultUseCase<UseCase.Params.Empty, Family?>() {
    override suspend fun doWork(params: UseCase.Params.Empty): Family? =
        withContext(AppCoroutineDispatchers.io) {
            val userRef = Firebase.firestore
                .collection(FirestoreCollection.Users.id)
                .document(Firebase.auth.currentUser?.uid.orEmpty())

            val familyDoc = Firebase.firestore
                .collection(FirestoreCollection.Family.id)
                .whereArrayContains(
                    FirestoreCollection.Family.Field.users.name,
                    userRef
                )
                .limit(1)
                .get().await()

            familyDoc.documents.firstOrNull()?.toObject(Family::class.java)
        }
}