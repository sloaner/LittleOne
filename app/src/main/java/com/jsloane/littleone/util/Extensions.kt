package com.jsloane.littleone.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

private tailrec fun Context.getContextActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getContextActivity()
    else -> null
}

val Context.activity: ComponentActivity?
    get() = getContextActivity()