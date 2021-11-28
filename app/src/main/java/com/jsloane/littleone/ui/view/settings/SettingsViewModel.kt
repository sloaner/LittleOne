package com.jsloane.littleone.ui.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.Companion.PreferenceKey
import com.jsloane.littleone.ui.view.feed.FeedAction
import com.jsloane.littleone.ui.view.feed.FeedViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    appSettingsRepository: AppSettingsRepository
) : ViewModel() {
    private val selectedFilters = MutableStateFlow(listOf<ActivityType>())
    private val selectedChild = MutableStateFlow("FkzZXXvYaIa3QS6IWr2P")

    val state: StateFlow<FeedViewState> = combine(
        selectedFilters,
        selectedChild
    ) { filters, child ->
        FeedViewState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedViewState.Empty
    )

    init {
        viewModelScope.launch {
            Firebase.auth.signOut()
            appSettingsRepository.setPreference(PreferenceKey.FAMILY, "")
        }
    }

    fun submitAction(action: FeedAction) {
        viewModelScope.launch {
            when (action) {
                is FeedAction.SignInEmail -> {}
                is FeedAction.SignInToken -> {}
                is FeedAction.UpdatePassword -> {}
                is FeedAction.UpdateSelectedFilters -> updateSelectedFilters(action.filters)
                else -> {}
            }
        }
    }

    private suspend fun updateSelectedFilters(filters: List<ActivityType>) {
        selectedFilters.emit(filters)
    }
}
