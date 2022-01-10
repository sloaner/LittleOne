package com.jsloane.littleone.data.repository

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.PreferenceKey
import com.jsloane.littleone.util.valueOf
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

    override val atAGlanceEnabled: Flow<Boolean>
        get() = settingsDataStore.data.map { it[PreferenceKey.GLANCE_ENABLED] ?: true }

    override val atAGlanceSlots: Flow<List<ActivityType.Category?>>
        get() = settingsDataStore.data.map {
            val slot1 = valueOf<ActivityType.Category>(it[PreferenceKey.GLANCE_SLOT1].orEmpty())
            val slot2 = valueOf<ActivityType.Category>(it[PreferenceKey.GLANCE_SLOT2].orEmpty())
            val slot3 = valueOf<ActivityType.Category>(it[PreferenceKey.GLANCE_SLOT3].orEmpty())
            listOf(slot1, slot2, slot3)
        }

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
