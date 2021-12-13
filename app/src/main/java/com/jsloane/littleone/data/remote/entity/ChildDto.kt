package com.jsloane.littleone.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.util.toLocalDate
import java.time.ZoneOffset

data class ChildDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val birthday: Timestamp = Timestamp.now()
) {
    fun toChild() = Child(
        id = id,
        name = name,
        birthday = birthday.toDate().toInstant().toLocalDate(ZoneOffset.UTC)
    )
}
