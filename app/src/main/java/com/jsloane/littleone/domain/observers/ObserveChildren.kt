package com.jsloane.littleone.domain.observers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.data.entities.Child
import com.jsloane.littleone.domain.LOFirestore
import com.jsloane.littleone.domain.ObservableUseCase
import com.jsloane.littleone.domain.UseCase
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ObserveChildren @Inject constructor(
    private val auth: FirebaseAuth
) : ObservableUseCase<ObserveChildren.Params, List<Child?>>() {
    override fun createObservable(params: Params): Flow<List<Child?>> = callbackFlow {
        val queryHandler = EventListener<QuerySnapshot> { value, error ->
            val list = value?.documents?.map { it.toObject(Child::class.java) } ?: emptyList()
            trySend(list)
        }

        val childrenListener = Firebase.firestore
            .collection(LOFirestore.Family.id)
            .document(params.family_id)
            .collection(LOFirestore.Child.id)
            .addSnapshotListener(queryHandler)

        awaitClose { childrenListener.remove() }
    }

    data class Params(val family_id: String) : UseCase.Params
}
