package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.ResultUseCase
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.Companion.PreferenceKey
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetFamilyIdUseCase @Inject constructor(
    private val repository: LittleOneRepository,
    private val appSettings: AppSettingsRepository
) : ResultUseCase<GetFamilyIdUseCase.Params, Result<String>>() {

    override fun doWork(params: Params): Flow<Result<String>> = flow {
        val cached = appSettings.familyId.first()
        if (cached.isNotBlank()) {
            emit(Result.Success(cached))
        } else {
            try {
                val response = repository
                    .findFamily(params.user_id)
                    .filterIsInstance<Result.Success<Family>>()
                    .first()
                appSettings.setPreference(PreferenceKey.FAMILY, response.data.id)
                emit(Result.Success(response.data.id))
            } catch (e: Throwable) {
                emit(Result.Error("Unable to find family"))
            }
        }
    }

    data class Params(
        val user_id: String
    ) : UseCase.Params
}
