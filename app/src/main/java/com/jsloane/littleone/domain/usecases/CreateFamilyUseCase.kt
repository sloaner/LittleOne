package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class CreateFamilyUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<CreateFamilyUseCase.Params, Result<String>>() {
    override fun doWork(params: Params): Flow<Result<String>> = flow {

        val idRes = repository.createFamily(params.user_id).last()

        when (idRes) {
            is Result.Success -> emit(Result.Success(idRes.data))
            else -> emit(Result.Error(""))
        }
    }

    data class Params(
        val user_id: String
    ) : UseCase.Params
}
