package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class CreateActivityUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<CreateActivityUseCase.Params, Result<String>>() {
    override fun doWork(params: Params): Flow<Result<String>> = flow {

        val idRes =
            repository.createActivity(params.family_id, params.child_id, params.activity).last()

        when (idRes) {
            is Result.Success -> emit(Result.Success(idRes.data))
            else -> emit(Result.Error(""))
        }
    }

    data class Params(
        val family_id: String,
        val child_id: String,
        val activity: Activity
    ) : UseCase.Params
}
