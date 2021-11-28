package com.jsloane.littleone.domain

import com.jsloane.littleone.base.Result
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

abstract class UseCase<in P : UseCase.Params> {
    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<Result<Unit>> {
        return flow {
            withTimeout(timeoutMs) {
                emit(Result.Loading())
                doWork(params)
                emit(Result.Success(Unit))
            }
        }.catch { t ->
            emit(Result.Error(t.message.orEmpty()))
        }
    }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }

    interface Params {
        object Empty : Params
    }
}

abstract class ResultUseCase<in P : UseCase.Params, R> {
    operator fun invoke(params: P): Flow<R> = doWork(params)

    protected abstract fun doWork(params: P): Flow<R>
}

abstract class ObservableUseCase<in P : UseCase.Params, T> {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow: Flow<Result<T>> = paramState
        .distinctUntilChanged()
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<Result<T>>
}
