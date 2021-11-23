package com.jsloane.littleone.domain.model

import java.time.Duration
import java.time.LocalDateTime

data class Activity(
    val id: String,
    val type: ActivityType,
    val start_time: LocalDateTime,
    val duration: Duration,
    val notes: String
)
