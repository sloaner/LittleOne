package com.jsloane.littleone.util

import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.HOUR_IN_MILLIS
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RelativeTimeFormatter {
    companion object {
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())

        fun format(then: Instant): String {
            val now = Instant.now()
            val diff = now.toEpochMilli() - then.toEpochMilli()
            return when {
                diff < 0 ->
                    "in the future"
                diff < 1 * MINUTE_IN_MILLIS ->
                    "moments ago"
                diff < 2 * MINUTE_IN_MILLIS ->
                    "a minute ago"
                diff < 60 * MINUTE_IN_MILLIS ->
                    "${diff / MINUTE_IN_MILLIS} minutes ago"
                diff < 61 * MINUTE_IN_MILLIS ->
                    "an hour ago"
                diff < 2 * HOUR_IN_MILLIS ->
                    "an hour and ${diff.mod(HOUR_IN_MILLIS) / MINUTE_IN_MILLIS} mins ago"
                diff < 12 * HOUR_IN_MILLIS ->
                    "${diff / HOUR_IN_MILLIS} hours " +
                        "${diff.mod(HOUR_IN_MILLIS) / MINUTE_IN_MILLIS} mins ago"
                diff < 1 * DAY_IN_MILLIS ->
                    "${diff / HOUR_IN_MILLIS} hours ago"
                else ->
                    timeFormatter.format(then).lowercase()
            }
        }
    }
}
