package com.jsloane.littleone.util

import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class RelativeTimeFormatter {
    companion object {
        fun format(then: Instant): String {
            val now = Instant.now()
            val duration = Duration.between(then, now)
            return when {
                duration.isNegative ->
                    "in the future"
                duration.toMinutes() < 1 ->
                    "moments ago"
                duration.toMinutes() < 2 ->
                    "a minute ago"
                duration.toMinutes() < 60 ->
                    "${duration.toMinutes()} minutes ago"
                duration.toMinutes() < 61 ->
                    "an hour ago"
                duration.toHours() < 2 ->
                    "an hour and ${duration.minutesPart} mins ago"
                duration.toDays() < 1 && duration.minutesPart == 0 ->
                    "${duration.toHours()} hours ago"
                duration.toDays() < 1 ->
                    "${duration.toHours()} hours ${duration.minutesPart} mins ago"
                else ->
                    Formatters.hour_minute_am.format(then).lowercase()
            }
        }

        fun format(then: LocalDate): String {
            val today = LocalDate.now()
            return when {
                then == today -> "Today"
                then == today.minusDays(1) -> "Yesterday"
                then.year == today.year -> Formatters.monthName_day.format(then)
                else -> Formatters.monthName_day_year.format(then)
            }
        }

        fun format(duration: Duration): String {
            return when {
                duration < Duration.ofMinutes(1L) ->
                    "${duration.seconds}s"
                duration < Duration.ofHours(1L) ->
                    "${duration.minutesPart.zeroPad()}m ${duration.secondsPart.zeroPad()}s"
                else ->
                    "${duration.hoursPart}h ${duration.minutesPart.zeroPad()}m"
            }
        }

        fun formatTimer(duration: Duration): String {
            return when {
                duration < Duration.ofHours(1L) ->
                    "${duration.minutesPart.zeroPad()}:${duration.secondsPart.zeroPad()}"
                else ->
                    "${duration.hoursPart.zeroPad()}:${duration.minutesPart.zeroPad()}"
            }
        }
    }
}
