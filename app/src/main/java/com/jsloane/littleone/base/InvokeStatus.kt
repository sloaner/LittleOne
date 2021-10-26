package com.jsloane.littleone.base

sealed class InvokeStatus {
    object Idle : InvokeStatus()
    object Started : InvokeStatus()
    object Success : InvokeStatus()
    data class Error(val throwable: Throwable) : InvokeStatus()
}
