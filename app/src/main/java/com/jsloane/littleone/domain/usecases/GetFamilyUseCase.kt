package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.PreferenceKey
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class GetFamilyUseCase @Inject constructor(
    private val repository: LittleOneRepository,
    private val appSettings: AppSettingsRepository
) : ResultUseCase<GetFamilyUseCase.Params, Result<Family>>() {

    override fun doWork(params: Params): Flow<Result<Family>> = flow {
        val cached = appSettings.familyId.first()
        if (cached.isNotBlank()) {
            emitAll(repository.getFamily(cached))
        } else {
            emitAll(
                repository.findFamily(params.user_id).onEach {
                    if (it is Result.Success<Family>) {
                        appSettings.setPreference(PreferenceKey.FAMILY, it.data.id)
                    }
                }
            )
        }
    }

    data class Params(
        val user_id: String
    ) : UseCase.Params
}
