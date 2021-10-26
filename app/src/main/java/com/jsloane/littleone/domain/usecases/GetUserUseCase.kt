package com.jsloane.littleone.domain.usecases

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.data.entities.User
import com.jsloane.littleone.domain.FirestoreCollection
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GetUserUseCase @Inject constructor() : ResultUseCase<UseCase.Params.Empty, User?>() {
    override suspend fun doWork(params: UseCase.Params.Empty): User? =
        withContext(AppCoroutineDispatchers.io) {
            val userDoc = Firebase.auth.currentUser?.uid?.let { userId ->
                Firebase.firestore.collection(FirestoreCollection.Family.id).document(userId).get()
                    .await()
            }
            userDoc?.toObject(User::class.java)
        }
}
