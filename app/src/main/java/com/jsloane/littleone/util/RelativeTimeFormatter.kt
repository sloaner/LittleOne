package com.jsloane.littleone.util

import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class RelativeTimeFormatter {
    companion object {
        fun format(then: Instant, now: Instant = Instant.now()): String {
            val duration = Duration.between(then, now)
            val absDuration = duration.abs()
            val base = when {
                absDuration.toMinutes() < 1 ->
                    "moments"
                absDuration.toMinutes() < 2 ->
                    "a minute"
                absDuration.toMinutes() < 60 ->
                    "${absDuration.toMinutes()} minutes"
                absDuration.toMinutes() < 61 ->
                    "an hour"
                absDuration.toHours() < 2 ->
                    "an hour and ${absDuration.minutesPart} mins"
                absDuration.toDays() < 1 && absDuration.minutesPart == 0 ->
                    "${absDuration.toHours()} hours"
                absDuration.toDays() < 1 ->
                    "${absDuration.toHours()} hours ${absDuration.minutesPart} mins"
                else ->
                    Formatters.hour_minute_am.format(then).lowercase()
            }

            return when {
                duration.toDays() < 0 -> base
                duration.isNegative -> "in $base"
                duration.toDays() < 1 -> "$base ago"
                else -> base
            }
        }

        fun format(then: LocalDate, today: LocalDate = LocalDate.now()): String {
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
                    "${duration.minutesPart}m ${duration.secondsPart.zeroPad()}s"
                else ->
                    "${duration.toHours()}h ${duration.minutesPart.zeroPad()}m"
            }
        }

        fun formatEstimate(duration: Duration): String {
            return when {
                duration < Duration.ofMinutes(1L) ->
                    "${duration.seconds} seconds"
                duration < Duration.ofHours(3L) ->
                    "${duration.toMinutes()} minutes"
                else ->
                    "${duration.toHours()} hours"
            }
        }

        fun formatTimer(duration: Duration): String {
            return when {
                duration < Duration.ofHours(1L) ->
                    "${duration.minutesPart.zeroPad()}:${duration.secondsPart.zeroPad()}"
                else ->
                    "${duration.toHours()}:${duration.minutesPart.zeroPad()}"
            }
        }
    }
}
