package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.last

class CreateFamilyUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<CreateFamilyUseCase.Params, Result<String>>() {
    override suspend fun doWork(params: Params): Result<String> {

        val idRes = repository.createFamily(params.user_id).last()

        return when (idRes) {
            is Result.Success -> Result.Success(idRes.data)
            else -> Result.Error("")
        }
    }

    data class Params(
        val user_id: String
    ) : UseCase.Params
}
