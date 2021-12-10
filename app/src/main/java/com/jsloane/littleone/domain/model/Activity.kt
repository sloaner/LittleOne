package com.jsloane.littleone.domain.model

import android.os.Parcelable
import java.time.Duration
import java.time.Instant
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activity(
    val id: String,
    val type: ActivityType,
    val start_time: Instant = Instant.now(),
    val duration: Duration = Duration.ZERO,
    val quantity: Float = 0f,
    val notes: String = ""
) : Parcelable
