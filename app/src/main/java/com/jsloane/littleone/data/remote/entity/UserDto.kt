package com.jsloane.littleone.data.remote.entity

import com.google.firebase.firestore.DocumentId

data class UserDto(
    @DocumentId val id: String = "",
    val family: String? = null
)
