package com.jsloane.littleone.data.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class Family(
    @DocumentId val id: String = "",
    val users: List<DocumentReference> = emptyList()
)
