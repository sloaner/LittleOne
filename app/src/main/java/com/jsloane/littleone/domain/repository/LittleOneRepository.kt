package com.jsloane.littleone.domain.repository

import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.domain.model.Family
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface LittleOneRepository {
    fun getFamily(familyId: String): Flow<Result<Family>>
    fun findFamily(userId: String): Flow<Result<Family>>
    fun findFamilyByInvite(inviteCode: String): Flow<Result<Family>>

    fun createFamily(userId: String): Flow<Result<String>>
    fun createFamilyInvite(
        familyId: String,
        inviteCode: String,
        inviteExpiration: Instant
    ): Flow<Result<Unit>>

    fun updateFamilyUsers(familyId: String, userId: String): Flow<Result<Unit>>
    fun createChild(familyId: String, name: String, birthday: LocalDate): Flow<Result<String>>
    fun createActivity(familyId: String, childId: String, activity: Activity): Flow<Result<String>>

    fun observeChildren(familyId: String): Flow<Result<List<Child>>>
    fun observeActivities(familyId: String, childId: String): Flow<Result<List<Activity>>>
}
