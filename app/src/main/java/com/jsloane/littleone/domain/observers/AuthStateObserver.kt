package com.jsloane.littleone.domain.observers

import com.google.firebase.auth.FirebaseAuth
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ObservableUseCase
import com.jsloane.littleone.domain.UseCase
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthStateObserver @Inject constructor(
    private val auth: FirebaseAuth
) : ObservableUseCase<UseCase.Params.Empty, Boolean>() {
    override fun createObservable(params: UseCase.Params.Empty): Flow<Result<Boolean>> =
        callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener {
                trySend(Result.Success(it.currentUser != null))
            }

            auth.addAuthStateListener(authStateListener)

            awaitClose {
                auth.removeAuthStateListener(authStateListener)
            }
        }
}
