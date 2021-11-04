package com.jsloane.littleone.domain.usecases

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.data.entities.Child
import com.jsloane.littleone.domain.FirestoreCollection
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CreateChildUseCase @Inject constructor(
    private val createFamilyUseCase: CreateFamilyUseCase
) :
    ResultUseCase<CreateChildUseCase.Params, Child?>() {
    override suspend fun doWork(params: Params): Child? =
        withContext(AppCoroutineDispatchers.io) {
            val familyId = params.family_id
                ?: createFamilyUseCase(UseCase.Params.Empty).first()?.id
                ?: return@withContext null

            val familyRef = Firebase.firestore
                .collection(FirestoreCollection.Family.id)
                .document(familyId)

            val childRef = familyRef
                .collection(FirestoreCollection.Child.id)
                .add(
                    Child(
                        name = params.name,
                        birthday = Timestamp(
                            params.birthday
                                .atStartOfDay(ZoneId.systemDefault())
                                .toEpochSecond(),
                            0
                        )
                    )
                )
                .await()

            val childDoc = childRef.get().await()
            return@withContext childDoc.toObject(Child::class.java)
        }

    data class Params(
        val family_id: String? = null,
        val name: String,
        val birthday: LocalDate
    ) : UseCase.Params
}
