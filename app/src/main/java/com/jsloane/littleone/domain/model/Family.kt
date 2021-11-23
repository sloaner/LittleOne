package com.jsloane.littleone.domain.model

import java.time.LocalDateTime

data class Family(
    val id: String,
    val users: List<String>,
    val inviteCode: String?,
    val inviteExpiration: LocalDateTime?
)
