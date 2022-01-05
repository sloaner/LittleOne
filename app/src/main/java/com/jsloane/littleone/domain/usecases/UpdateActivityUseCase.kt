package com.jsloane.littleone.domain.usecases

import androidx.work.WorkManager
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class UpdateActivityUseCase @Inject constructor(
    private val repository: LittleOneRepository,
    private val workManager: WorkManager
) : ResultUseCase<UpdateActivityUseCase.Params, Result<Unit>>() {
    override fun doWork(params: Params): Flow<Result<Unit>> = flow {

        val res =
            repository.updateActivity(params.family_id, params.child_id, params.activity).last()

        when (res) {
            is Result.Success -> {
                if (!params.activity.isTimerRunning) {
                    workManager.cancelUniqueWork(params.activity.id)
                }
                emit(Result.Success(Unit))
            }
            else -> emit(Result.Error(""))
        }
    }

    data class Params(
        val family_id: String,
        val child_id: String,
        val activity: Activity
    ) : UseCase.Params
}
