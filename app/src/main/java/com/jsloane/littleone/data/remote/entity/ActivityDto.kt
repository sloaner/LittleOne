package com.jsloane.littleone.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import java.time.Duration
import java.time.ZoneOffset

data class ActivityDto(
    @DocumentId val id: String = "",
    val type: String = "",
    val start_time: Timestamp = Timestamp.now(),
    val duration: Long = 0,
    val notes: String = ""
) {
    fun toActivity() = Activity(
        id = id,
        type = ActivityType.values().firstOrNull { it.code == type } ?: ActivityType.SLEEP,
        start_time = start_time.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(),
        duration = Duration.ofSeconds(duration),
        notes = notes
    )
}
