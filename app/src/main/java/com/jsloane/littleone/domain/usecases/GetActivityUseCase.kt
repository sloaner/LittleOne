package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetActivityUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : ResultUseCase<GetActivityUseCase.Params, Result<Activity>>() {

    override fun doWork(params: Params): Flow<Result<Activity>> = flow {
        emitAll(repository.getActivity(params.family_id, params.child_id, params.activity_id))
    }

    data class Params(
        val family_id: String,
        val child_id: String,
        val activity_id: String
    ) : UseCase.Params
}
