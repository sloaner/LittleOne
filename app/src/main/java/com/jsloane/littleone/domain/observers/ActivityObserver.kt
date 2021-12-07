package com.jsloane.littleone.domain.observers

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ObservableUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ActivityObserver @Inject constructor(
    private val repository: LittleOneRepository
) : ObservableUseCase<ActivityObserver.Params, List<Activity>>() {
    override fun createObservable(params: Params): Flow<Result<List<Activity>>> =
        repository.observeActivities(params.family_id, params.child_id)

    data class Params(
        val family_id: String,
        val child_id: String,
        val date: Instant = Instant.now()
    ) : UseCase.Params
}
