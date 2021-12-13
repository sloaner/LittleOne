package com.jsloane.littleone.data.repository

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.Companion.PreferenceKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingsRepositoryImpl(
    @ApplicationContext appContext: Context
) : AppSettingsRepository {
    private val Context.dataStore by preferencesDataStore("settings")
    private val settingsDataStore = appContext.dataStore

    override val familyId: Flow<String>
        get() = getPreference(PreferenceKey.FAMILY, "")

    override val childId: Flow<String>
        get() = getPreference(PreferenceKey.CHILD, "")

    override fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return settingsDataStore.data.map { it[key] ?: defaultValue }
    }

    override suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        settingsDataStore.edit { it[key] = value }
    }

    override suspend fun <T> setPreference(
        key: Preferences.Key<T>,
        update: (MutablePreferences) -> Unit
    ) {
        settingsDataStore.edit { update(it) }
    }
}
