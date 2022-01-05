package com.jsloane.littleone.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private tailrec fun Context.getContextActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getContextActivity()
    else -> null
}

val Context.activity: ComponentActivity?
    get() = getContextActivity()

val Duration.secondsPart get() = (this.seconds % 60).toInt()
val Duration.minutesPart get() = (this.toMinutes() % 60).toInt()
val Duration.hoursPart get() = (this.toHours() % 24).toInt()
fun Instant.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()) = this.atZone(zoneId).toLocalDate()
fun Instant.toLocalTime(zoneId: ZoneId = ZoneId.systemDefault()) = this.atZone(zoneId).toLocalTime()
fun Instant.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()) =
    this.atZone(zoneId).toLocalDateTime()

fun Int.zeroPad(minLength: Int = 2): String = this.toString().padStart(minLength, '0')

fun Any.equalsAny(vararg comparisons: Any) = comparisons.any { it == this }

fun BroadcastReceiver.goAsync(
    coroutineScope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> Unit
) {
    val result = goAsync()
    coroutineScope.launch(dispatcher) {
        try {
            block()
        } finally {
            // Always call finish(), even if the coroutineScope was cancelled
            result.finish()
        }
    }
}
