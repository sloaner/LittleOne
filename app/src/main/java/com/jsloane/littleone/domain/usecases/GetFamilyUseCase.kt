package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFamilyUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<GetFamilyUseCase.Params, Flow<Result<Family>>>() {

    override suspend fun doWork(params: Params): Flow<Result<Family>> {
        return repository.findFamily(params.user_id)
    }

    data class Params(
        val user_id: String
    ) : UseCase.Params
}
