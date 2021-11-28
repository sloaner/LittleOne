package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.lastOrNull

class CreateChildUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : UseCase<CreateChildUseCase.Params>() {
    override suspend fun doWork(params: Params) {
        val family = repository.getFamily(params.family_id).lastOrNull()

        when (family) {
            is Result.Error -> throw Exception("")
            is Result.Loading -> throw Exception("")
            else -> {}
        }

        repository.createChild(params.family_id, params.name, params.birthday)
    }

    data class Params(
        val family_id: String,
        val name: String,
        val birthday: LocalDate
    ) : UseCase.Params
}
