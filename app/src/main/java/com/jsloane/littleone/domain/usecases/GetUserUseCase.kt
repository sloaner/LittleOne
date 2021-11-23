package com.jsloane.littleone.domain.usecases

import com.google.firebase.firestore.auth.User
import com.jsloane.littleone.base.AppCoroutineDispatchers
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import javax.inject.Inject
import kotlinx.coroutines.withContext

class GetUserUseCase @Inject constructor() : ResultUseCase<UseCase.Params.Empty, User?>() {
    override suspend fun doWork(params: UseCase.Params.Empty): User? =
        withContext(AppCoroutineDispatchers.io) {
            null
        }
}
