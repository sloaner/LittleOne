package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last

class CreateChildUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : UseCase<CreateChildUseCase.Params>() {
    override suspend fun doWork(params: Params) {
        repository.getFamily(params.family_id)
            .filterIsInstance<Result.Success<Family>>()
            .firstOrNull() ?: throw Exception("")

        repository.createChild(params.family_id, params.name, params.birthday).last()
    }

    data class Params(
        val family_id: String,
        val name: String,
        val birthday: LocalDate
    ) : UseCase.Params
}
