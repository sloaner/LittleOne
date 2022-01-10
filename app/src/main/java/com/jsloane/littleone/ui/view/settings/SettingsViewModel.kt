package com.jsloane.littleone.ui.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.Family
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.PreferenceKey
import com.jsloane.littleone.domain.usecases.DeleteInviteCodeUseCase
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import com.jsloane.littleone.domain.usecases.UpdateInviteCodeUseCase
import com.jsloane.littleone.navigation.Destination
import com.jsloane.littleone.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val getFamilyUseCase: GetFamilyUseCase,
    private val updateInviteCodeUseCase: UpdateInviteCodeUseCase,
    private val deleteInviteCodeUseCase: DeleteInviteCodeUseCase
) : ViewModel() {

    private val currentFamily = MutableStateFlow(Family("", emptyList(), null, null))

    val state: StateFlow<SettingsViewState> = combine(
        currentFamily,
        appSettingsRepository.atAGlanceEnabled,
        appSettingsRepository.atAGlanceSlots
    ) { family, glance, slots ->
        SettingsViewState(
            family = family,
            glanceEnabled = glance,
            glanceSlots = slots,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsViewState.Empty
    )

    init {
        getFamily()
    }

    fun submitAction(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                SettingsAction.Logout -> doLogout()

                is SettingsAction.RefreshCode -> refreshInviteCode()
                is SettingsAction.DeleteCode -> deleteInviteCode()

                is SettingsAction.UpdateGlanceEnabled -> updateGlanceEnabled(action.enabled)
                is SettingsAction.UpdateGlanceSlot1 -> updateGlanceSlot(1, action.category)
                is SettingsAction.UpdateGlanceSlot2 -> updateGlanceSlot(2, action.category)
                is SettingsAction.UpdateGlanceSlot3 -> updateGlanceSlot(3, action.category)
            }
        }
    }

    private fun doLogout() {
        viewModelScope.launch {
            appSettingsRepository.setPreference(PreferenceKey.CHILD, "")
            appSettingsRepository.setPreference(PreferenceKey.FAMILY, "")
            Firebase.auth.signOut()
            NavigationManager.navigate(Destination.Feed)
        }
    }

    private fun getFamily() {
        viewModelScope.launch {
            getFamilyUseCase(GetFamilyUseCase.Params(Firebase.auth.uid.orEmpty())).collect {
                when (it) {
                    is Result.Error -> {}
                    is Result.Loading -> {}
                    is Result.Success -> {
                        currentFamily.value = it.data
                    }
                }
            }
        }
    }

    private fun refreshInviteCode() {
        viewModelScope.launch {
            appSettingsRepository.familyId.collect { familyId ->
                updateInviteCodeUseCase(UpdateInviteCodeUseCase.Params(familyId))
                    .filterIsInstance<Result.Success<Unit>>()
                    .collect {
                        getFamily()
                    }
            }
        }
    }

    private fun deleteInviteCode() {
        viewModelScope.launch {
            appSettingsRepository.familyId.collect { familyId ->
                deleteInviteCodeUseCase(DeleteInviteCodeUseCase.Params(familyId))
                    .filterIsInstance<Result.Success<Unit>>()
                    .collect {
                        getFamily()
                    }
            }
        }
    }

    private fun updateGlanceEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.setPreference(PreferenceKey.GLANCE_ENABLED, enabled)
        }
    }

    private fun updateGlanceSlot(slot_num: Int, category: ActivityType.Category?) {
        val key = when (slot_num) {
            1 -> PreferenceKey.GLANCE_SLOT1
            2 -> PreferenceKey.GLANCE_SLOT2
            3 -> PreferenceKey.GLANCE_SLOT3
            else -> return
        }
        viewModelScope.launch {
            appSettingsRepository.setPreference(key, category?.name.orEmpty())
        }
    }
}
