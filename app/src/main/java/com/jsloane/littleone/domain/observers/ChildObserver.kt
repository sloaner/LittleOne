package com.jsloane.littleone.domain.observers

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ObservableUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ChildObserver @Inject constructor(
    private val repository: LittleOneRepository
) : ObservableUseCase<ChildObserver.Params, List<Child>>() {
    override fun createObservable(params: Params): Flow<Result<List<Child>>> =
        repository.observeChildren(params.family_id)

    data class Params(val family_id: String) : UseCase.Params
}
