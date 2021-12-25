package com.jsloane.littleone.util

import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Formatters {
    val monthName_day: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMMM d").withZone(ZoneId.systemDefault())

    val monthName_day_year: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMMM dd, yyyy").withZone(ZoneId.systemDefault())

    val time_month_day_year: DateTimeFormatter =
        DateTimeFormatter.ofPattern("h:mm a 'on' MMM d, yyyy ").withZone(ZoneId.systemDefault())

    val hour_minute_am: DateTimeFormatter =
        DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())

    val hour_minute: DateTimeFormatter =
        DateTimeFormatter.ofPattern("hh:mm").withZone(ZoneId.systemDefault())

    val squishedDate: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMddyyyy").withZone(ZoneId.systemDefault())
}
