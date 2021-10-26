package com.jsloane.littleone.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object AppCoroutineDispatchers {
    val io: CoroutineDispatcher = Dispatchers.IO
    val computation: CoroutineDispatcher = Dispatchers.Default
    val main: CoroutineDispatcher = Dispatchers.Main
}
