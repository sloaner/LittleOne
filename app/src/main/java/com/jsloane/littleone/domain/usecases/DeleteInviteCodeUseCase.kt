package com.jsloane.littleone.domain.usecases

import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.repository.LittleOneRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.last

class DeleteInviteCodeUseCase @Inject constructor(
    private val repository: LittleOneRepository
) : UseCase<DeleteInviteCodeUseCase.Params>() {

    override suspend fun doWork(params: Params) {
        repository.deleteInviteCode(familyId = params.family_id).last()
    }

    data class Params(
        val family_id: String
    ) : UseCase.Params
}
