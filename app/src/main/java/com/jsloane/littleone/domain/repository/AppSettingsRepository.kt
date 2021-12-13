package com.jsloane.littleone.domain.repository

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    val familyId: Flow<String>
    val childId: Flow<String>

    fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)
    suspend fun <T> setPreference(key: Preferences.Key<T>, update: (MutablePreferences) -> Unit)

    companion object {
        object PreferenceKey {
            val FAMILY = stringPreferencesKey("FAMILY_KEY")
            val CHILD = stringPreferencesKey("CHILD_KEY")
        }
    }
}
