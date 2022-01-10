package com.jsloane.littleone.domain.repository

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jsloane.littleone.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    val familyId: Flow<String>
    val childId: Flow<String>

    val atAGlanceEnabled: Flow<Boolean>
    val atAGlanceSlots: Flow<List<ActivityType.Category?>>

    fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)
    suspend fun <T> setPreference(key: Preferences.Key<T>, update: (MutablePreferences) -> Unit)

    object PreferenceKey {
        val FAMILY = stringPreferencesKey("FAMILY_KEY")
        val CHILD = stringPreferencesKey("CHILD_KEY")

        val GLANCE_ENABLED = booleanPreferencesKey("GLANCE_ENABLED")
        val GLANCE_SLOT1 = stringPreferencesKey("GLANCE_SLOT_1")
        val GLANCE_SLOT2 = stringPreferencesKey("GLANCE_SLOT_2")
        val GLANCE_SLOT3 = stringPreferencesKey("GLANCE_SLOT_3")
    }
}
