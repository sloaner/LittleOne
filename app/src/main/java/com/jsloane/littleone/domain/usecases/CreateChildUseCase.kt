package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.lastOrNull

class CreateChildUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<CreateChildUseCase.Params, Result<Unit>>() {
    override suspend fun doWork(params: Params): Result<Unit> {
        val family = repository.getFamily(params.family_id).lastOrNull()

        when (family) {
            is Result.Error -> return Result.Error("")
            is Result.Loading -> return Result.Error("")
            else -> {}
        }

        repository.createChild(params.family_id, params.name, params.birthday)

        return Result.Success(Unit)
    }

    data class Params(
        val family_id: String,
        val name: String,
        val birthday: LocalDate
    ) : UseCase.Params
}
