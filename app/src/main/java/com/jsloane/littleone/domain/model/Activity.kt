package com.jsloane.littleone.domain.model

import java.time.Duration
import java.time.Instant

data class Activity(
    val id: String,
    val type: ActivityType,
    val start_time: Instant = Instant.now(),
    val duration: Duration = Duration.ZERO,
    val quantity: Float = 0f,
    val notes: String = ""
)
