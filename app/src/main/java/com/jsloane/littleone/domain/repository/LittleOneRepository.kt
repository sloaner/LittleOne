package com.jsloane.littleone.domain.repository

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.domain.model.Family
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface LittleOneRepository {
    fun findFamily(userId: String): Flow<Result<Family>>
    fun getFamily(familyId: String): Flow<Result<Family>>
    fun createFamily(userId: String): Flow<Result<String>>
    fun updateFamilyUsers(familyId: String, userId: String): Flow<Result<Unit>>

    fun createChild(familyId: String, name: String, birthday: LocalDate): Flow<Result<String>>

    fun getActivity(familyId: String, childId: String, activityId: String): Flow<Result<Activity>>
    fun createActivity(familyId: String, childId: String, activity: Activity): Flow<Result<String>>
    fun updateActivity(familyId: String, childId: String, activity: Activity): Flow<Result<Unit>>
    fun deleteActivity(familyId: String, childId: String, activityId: String): Flow<Result<Unit>>

    fun findFamilyByInvite(inviteCode: String): Flow<Result<Family>>
    fun createInviteCode(
        familyId: String,
        inviteCode: String,
        inviteExpiration: Instant
    ): Flow<Result<Unit>>
    fun deleteInviteCode(familyId: String): Flow<Result<Unit>>

    fun observeChildren(familyId: String): Flow<Result<List<Child>>>
    fun observeActivities(
        familyId: String,
        childId: String,
        after: Instant = Instant.EPOCH
    ): Flow<Result<List<Activity>>>
}
