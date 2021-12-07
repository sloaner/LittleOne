package com.jsloane.littleone.domain.model

import java.time.Duration
import java.time.LocalDateTime

data class Activity(
    val id: String,
    val type: ActivityType,
    val start_time: LocalDateTime = LocalDateTime.now(),
    val duration: Duration = Duration.ZERO,
    val quantity: Float = 0f,
    val notes: String = ""
)
