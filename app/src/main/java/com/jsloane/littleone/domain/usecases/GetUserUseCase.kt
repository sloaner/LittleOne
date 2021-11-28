package com.jsloane.littleone.domain.usecases

import com.google.firebase.firestore.auth.User
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserUseCase @Inject constructor() : ResultUseCase<UseCase.Params.Empty, User?>() {
    override fun doWork(params: UseCase.Params.Empty): Flow<User?> = flow {}
}
