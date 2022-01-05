package com.jsloane.littleone.domain.usecases

import androidx.work.WorkManager
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class DeleteActivityUseCase @Inject constructor(
    private val repository: LittleOneRepository,
    private val workManager: WorkManager
) : ResultUseCase<DeleteActivityUseCase.Params, Result<Unit>>() {
    override fun doWork(params: Params): Flow<Result<Unit>> = flow {

        val res =
            repository.deleteActivity(params.family_id, params.child_id, params.activityId).last()

        when (res) {
            is Result.Success -> {
                workManager.cancelUniqueWork(params.activityId)
                emit(Result.Success(Unit))
            }
            else -> emit(Result.Error(""))
        }
    }

    data class Params(
        val family_id: String,
        val child_id: String,
        val activityId: String
    ) : UseCase.Params
}
