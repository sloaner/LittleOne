package com.jsloane.littleone.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import java.time.Instant
import java.time.ZoneId

private tailrec fun Context.getContextActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getContextActivity()
    else -> null
}

val Context.activity: ComponentActivity?
    get() = getContextActivity()

fun Instant.toLocalDateTime(zoneId: ZoneId) = this.atZone(zoneId).toLocalDateTime()
fun Instant.toLocalDate(zoneId: ZoneId) = this.atZone(zoneId).toLocalDate()
fun Instant.toLocalTime(zoneId: ZoneId) = this.atZone(zoneId).toLocalTime()
