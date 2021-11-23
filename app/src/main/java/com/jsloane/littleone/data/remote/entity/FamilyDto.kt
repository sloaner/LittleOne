package com.jsloane.littleone.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.jsloane.littleone.domain.model.Family
import java.time.ZoneOffset

data class FamilyDto(
    @DocumentId val id: String = "",
    val users: List<DocumentReference> = emptyList(),
    val inviteCode: String? = null,
    val inviteExpiration: Timestamp? = null
) {
    fun toFamily() = Family(
        id = id,
        users = users.map { it.id },
        inviteCode = inviteCode,
        inviteExpiration = inviteExpiration?.toDate()
            ?.toInstant()
            ?.atZone(ZoneOffset.UTC)
            ?.toLocalDateTime()
    )
}
