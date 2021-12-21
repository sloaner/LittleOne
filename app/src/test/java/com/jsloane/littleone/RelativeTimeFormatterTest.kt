package com.jsloane.littleone

import com.jsloane.littleone.util.RelativeTimeFormatter
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class RelativeTimeFormatterTest {
    @Test
    fun assertPastFormats() {
        val now = Instant.EPOCH
        assertEquals(
            "moments ago",
            RelativeTimeFormatter.format(now.minus(0, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "moments ago",
            RelativeTimeFormatter.format(now.minus(30, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "a minute ago",
            RelativeTimeFormatter.format(now.minus(60, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "a minute ago",
            RelativeTimeFormatter.format(now.minus(90, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "2 minutes ago",
            RelativeTimeFormatter.format(now.minus(2, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "59 minutes ago",
            RelativeTimeFormatter.format(now.minus(59, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "an hour ago",
            RelativeTimeFormatter.format(now.minus(60, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "an hour and 20 mins ago",
            RelativeTimeFormatter.format(now.minus(80, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "3 hours ago",
            RelativeTimeFormatter.format(now.minus(3, ChronoUnit.HOURS), now)
        )
        assertEquals(
            "3 hours 23 mins ago",
            RelativeTimeFormatter.format(now.minus(203, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "23 hours 59 mins ago",
            RelativeTimeFormatter.format(now.minus(1439, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "06:00 pm",
            RelativeTimeFormatter.format(now.minus(25, ChronoUnit.HOURS), now)
        )
    }

    @Test
    fun assertFutureFormats() {
        val now = Instant.EPOCH
        assertEquals(
            "moments ago",
            RelativeTimeFormatter.format(now.plus(0, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "in moments",
            RelativeTimeFormatter.format(now.plus(30, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "in a minute",
            RelativeTimeFormatter.format(now.plus(60, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "in a minute",
            RelativeTimeFormatter.format(now.plus(90, ChronoUnit.SECONDS), now)
        )
        assertEquals(
            "in 2 minutes",
            RelativeTimeFormatter.format(now.plus(2, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "in 59 minutes",
            RelativeTimeFormatter.format(now.plus(59, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "in an hour",
            RelativeTimeFormatter.format(now.plus(60, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "in an hour and 20 mins",
            RelativeTimeFormatter.format(now.plus(80, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "in 3 hours",
            RelativeTimeFormatter.format(now.plus(3, ChronoUnit.HOURS), now)
        )
        assertEquals(
            "in 3 hours 23 mins",
            RelativeTimeFormatter.format(now.plus(203, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "in 23 hours 59 mins",
            RelativeTimeFormatter.format(now.plus(1439, ChronoUnit.MINUTES), now)
        )
        assertEquals(
            "08:00 pm",
            RelativeTimeFormatter.format(now.plus(25, ChronoUnit.HOURS), now)
        )
    }
}
