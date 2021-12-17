package com.jsloane.littleone.ui.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.Companion.PreferenceKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    appSettingsRepository: AppSettingsRepository
) : ViewModel() {
    private val selectedFilters = MutableStateFlow(listOf<ActivityType>())

    init {
        viewModelScope.launch {
            appSettingsRepository.setPreference(PreferenceKey.CHILD, "")
            appSettingsRepository.setPreference(PreferenceKey.FAMILY, "")
            Firebase.auth.signOut()
        }
    }

    fun submitAction(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                SettingsAction.OpenLogin -> TODO()
                SettingsAction.OpenSettings -> TODO()

                is SettingsAction.ChangeTimeframe -> TODO()
                is SettingsAction.DeleteActivity -> TODO()
                is SettingsAction.EditActivity -> TODO()
                is SettingsAction.UpdateSelectedFilters -> TODO()
                is SettingsAction.AddNewActivity -> TODO()
            }
        }
    }
}
