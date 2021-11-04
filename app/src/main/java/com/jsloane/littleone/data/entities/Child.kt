package com.jsloane.littleone.data.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Child(
    @DocumentId val id: String = "",
    @PropertyName("firstName") val name: String = "",
    val birthday: Timestamp = Timestamp.now()
)
