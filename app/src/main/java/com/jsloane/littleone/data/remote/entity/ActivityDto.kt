package com.jsloane.littleone.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import java.time.Duration
import java.time.ZoneId

data class ActivityDto(
    @DocumentId val id: String = "",
    val type: String = "",
    val start_time: Timestamp = Timestamp.now(),
    val duration: Long = 0,
    val quantity: Float = 0f,
    val notes: String = ""
) {
    fun toActivity() = Activity(
        id = id,
        type = ActivityType.values().firstOrNull { it.code == type } ?: ActivityType.SLEEP,
        start_time = start_time.toDate().toInstant().atZone(ZoneId.systemDefault())
            .toLocalDateTime(),
        duration = Duration.ofSeconds(duration),
        quantity = quantity,
        notes = notes
    )

    companion object {
        fun fromActivity(activity: Activity) = ActivityDto(
            id = activity.id,
            type = activity.type.code,
            start_time = Timestamp(
                activity.start_time.atZone(ZoneId.systemDefault()).toEpochSecond(), 0
            ),
            duration = activity.duration.seconds,
            quantity = activity.quantity,
            notes = activity.notes
        )
    }
}
