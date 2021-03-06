package com.jsloane.littleone.data.repository

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.data.remote.LittleOneApi
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.LittleOneRepository
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class LittleOneRepositoryImpl @Inject constructor(
    private val api: LittleOneApi
) : LittleOneRepository {

    override fun getFamily(
        familyId: String
    ): Flow<Result<Family>> = flow {
        emit(Result.Loading())
        api.getFamily(familyId)?.let {
            emit(Result.Success(it))
        } ?: emit(Result.Error("Error!"))
    }

    override fun findFamily(
        userId: String
    ): Flow<Result<Family>> = flow {
        emit(Result.Loading())
        api.findFamilyByUser(userId)?.let {
            emit(Result.Success(it))
        } ?: emit(Result.Error("Error!"))
    }

    override fun findFamilyByInvite(
        inviteCode: String
    ): Flow<Result<Family>> = flow {
        emit(Result.Loading())
        api.findFamilyByInviteCode(inviteCode)?.let {
            // TODO: Workaround for Firestore bug
            if (it.inviteExpiration?.isBefore(Instant.now()) == true)
                null
            else
                emit(Result.Success(it))
        } ?: emit(Result.Error("Error!"))
    }

    override fun createFamily(
        userId: String
    ): Flow<Result<String>> = flow {
        emit(Result.Loading())
        val id = api.createFamily(userId)
        emit(Result.Success(id))
    }

    override fun createInviteCode(
        familyId: String,
        inviteCode: String,
        inviteExpiration: Instant
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        api.updateFamilyInviteCode(familyId, inviteCode, inviteExpiration)
        emit(Result.Success(Unit))
    }

    override fun deleteInviteCode(familyId: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        api.updateFamilyInviteCode(familyId, "", Instant.EPOCH)
        emit(Result.Success(Unit))
    }

    override fun updateFamilyUsers(
        familyId: String,
        userId: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        api.updateFamilyUsers(familyId, userId)
        emit(Result.Success(Unit))
    }

    override fun createChild(
        familyId: String,
        name: String,
        birthday: LocalDate
    ): Flow<Result<String>> = flow {
        emit(Result.Loading())
        val id = api.addChild(familyId, name, birthday)
        emit(Result.Success(id))
    }

    override fun getActivity(
        familyId: String,
        childId: String,
        activityId: String
    ): Flow<Result<Activity>> = flow {
        emit(Result.Loading())
        api.getActivity(familyId, childId, activityId)?.let {
            emit(Result.Success(it))
        } ?: emit(Result.Error("Error!"))
    }

    override fun createActivity(
        familyId: String,
        childId: String,
        activity: Activity
    ): Flow<Result<String>> = flow {
        emit(Result.Loading())
        val id = api.addActivity(familyId, childId, activity)
        emit(Result.Success(id))
    }

    override fun updateActivity(
        familyId: String,
        childId: String,
        activity: Activity
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        api.updateActivity(familyId, childId, activity)
        emit(Result.Success(Unit))
    }

    override fun deleteActivity(
        familyId: String,
        childId: String,
        activityId: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        api.deleteActivity(familyId, childId, activityId)
        emit(Result.Success(Unit))
    }

    override fun observeChildren(
        familyId: String
    ): Flow<Result<List<Child>>> =
        merge(
            flow { emit(Result.Loading(listOf<Child>())) },
            api.observeChildren(familyId).map {
                Result.Success(it.filterNotNull())
            }
        )

    override fun observeActivities(
        familyId: String,
        childId: String,
        after: Instant
    ): Flow<Result<List<Activity>>> =
        merge(
            flow { emit(Result.Loading(listOf<Activity>())) },
            api.observeActivities(familyId, childId, after).map {
                Result.Success(it.filterNotNull())
            }
        )
}
