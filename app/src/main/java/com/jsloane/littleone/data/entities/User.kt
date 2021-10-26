package com.jsloane.littleone.data.entities

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val family: String? = null
)
