package com.jsloane.littleone.base

sealed class Result<T>() {
    class Loading<T>(val data: T? = null) : Result<T>()
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val message: String, val data: T? = null) : Result<T>()
}
