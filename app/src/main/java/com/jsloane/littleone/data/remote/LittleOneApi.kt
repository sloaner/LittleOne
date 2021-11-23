package com.jsloane.littleone.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.data.remote.entity.ActivityDto
import com.jsloane.littleone.data.remote.entity.ChildDto
import com.jsloane.littleone.data.remote.entity.FamilyDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LittleOneApi {
    suspend fun getFamily(familyId: String): FamilyDto? {
        val familyDoc = Firebase.firestore
            .collection(Collections.Family.id)
            .document(familyId)
            .get()
            .await()

        return familyDoc.toObject()
    }

    suspend fun findFamilyByUser(userId: String): FamilyDto? {
        val userRef = Firebase.firestore
            .collection(Collections.User.id)
            .document(userId)

        val familyDoc = Firebase.firestore
            .collection(Collections.Family.id)
            .whereArrayContains(
                Collections.Family.Field.users,
                userRef
            )
            .limit(1)
            .get()
            .await()

        return familyDoc.documents.firstOrNull()?.toObject()
    }

    suspend fun findFamilyByInviteCode(inviteCode: String): FamilyDto? {
        val inviteFamilyDoc = Firebase.firestore
            .collection(Collections.Family.id)
            .whereEqualTo(
                Collections.Family.Field.inviteCode,
                inviteCode
            )
            .whereGreaterThanOrEqualTo(
                Collections.Family.Field.inviteExpiration,
                Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), 0)
            )
            .get()
            .await()

        return inviteFamilyDoc.documents.firstOrNull()?.toObject()
    }

    suspend fun createFamily(userId: String): String {
        val userRef = Firebase.firestore
            .collection(Collections.User.id)
            .document(userId)

        val familyDoc = Firebase.firestore
            .collection(Collections.Family.id)
            .add(FamilyDto(users = listOf(userRef)))
            .await()

        return familyDoc.id
    }

    suspend fun updateFamilyInviteCode(
        familyId: String,
        inviteCode: String,
        inviteExpiration: LocalDateTime
    ) {
        Firebase.firestore
            .collection(Collections.Family.id)
            .document(familyId)
            .update(
                mapOf(
                    Collections.Family.Field.inviteCode to inviteCode,
                    Collections.Family.Field.inviteExpiration to
                        Timestamp(inviteExpiration.toEpochSecond(ZoneOffset.UTC), 0)
                )
            ).await()
    }

    suspend fun updateFamilyUsers(familyId: String, userId: String) {
        Firebase.firestore
            .collection(Collections.Family.id)
            .document(familyId)
            .update(
                Collections.Family.Field.users,
                FieldValue.arrayUnion(userId)
            ).await()
    }

    suspend fun addChild(familyId: String, name: String, birthday: LocalDate): String {
        val childRef = Firebase.firestore
            .collection(Collections.Family.id)
            .document(familyId)
            .collection(Collections.Child.id)
            .add(
                ChildDto(
                    name = name,
                    birthday = Timestamp(
                        birthday.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0
                    )
                )
            )
            .await()

        return childRef.id
    }

    fun observeChildren(familyId: String): Flow<List<ChildDto?>> = callbackFlow {
        val queryHandler = EventListener<QuerySnapshot> { value, error ->
            val list: List<ChildDto?> = value?.documents?.map { it.toObject() } ?: emptyList()
            trySend(list)
        }

        val childrenListener = Firebase.firestore
            .collection(Collections.Family.id)
            .document(familyId)
            .collection(Collections.Child.id)
            .orderBy(Collections.Child.Field.birthday, Query.Direction.ASCENDING)
            .addSnapshotListener(queryHandler)

        awaitClose { childrenListener.remove() }
    }

    fun observeActivities(familyId: String, childId: String): Flow<List<ActivityDto?>> =
        callbackFlow {
            val queryHandler = EventListener<QuerySnapshot> { value, error ->
                val list: List<ActivityDto?> =
                    value?.documents?.map { it.toObject() } ?: emptyList()
                trySend(list)
            }

            val childrenListener = Firebase.firestore
                .collection(Collections.Family.id)
                .document(familyId)
                .collection(Collections.Child.id)
                .document(childId)
                .collection(Collections.Activity.id)
                .orderBy(Collections.Activity.Field.start_time, Query.Direction.DESCENDING)
                .addSnapshotListener(queryHandler)

            awaitClose { childrenListener.remove() }
        }
}

private sealed class Collections(val id: String) {
    object User : Collections("user") {
        object Field {
            const val family = "family"
        }
    }

    object Family : Collections("family") {
        object Field {
            const val users = "users"
            const val inviteCode = "inviteCode"
            const val inviteExpiration = "inviteExpiration"
        }
    }

    object Child : Collections("child") {
        object Field {
            const val firstName = "firstName"
            const val birthday = "birthday"
        }
    }

    object Activity : Collections("activity") {
        object Field {
            const val type = "type"
            const val start_time = "start_time"
            const val duration = "duration"
            const val notes = "notes"
        }
    }
}
