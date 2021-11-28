package com.jsloane.littleone.ui.view.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.UseCase
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.observers.ActivityObserver
import com.jsloane.littleone.domain.observers.AuthStateObserver
import com.jsloane.littleone.domain.observers.ChildObserver
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    authStateObserver: AuthStateObserver,
    getFamily: GetFamilyUseCase,
    childObserver: ChildObserver,
    activityObserver: ActivityObserver,
    appSettingsRepository: AppSettingsRepository
) : ViewModel() {
    private val selectedFilters = MutableStateFlow(listOf<ActivityType>())
    private val selectedChild = MutableStateFlow("FkzZXXvYaIa3QS6IWr2P")

    val state: StateFlow<FeedViewState> = combine(
        authStateObserver.flow,
        activityObserver.flow.filter { it is Result.Success },
        selectedFilters,
        selectedChild
    ) { authState, activities, filters, child ->
        FeedViewState(
            selectedFilters = filters,
            selectedChild = child,
            activities = (activities as Result.Success<List<Activity>>).data.filter {
                selectedFilters.value.isEmpty() || selectedFilters.value.contains(it.type)
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedViewState.Empty
    )

    init {
        authStateObserver(UseCase.Params.Empty)

        viewModelScope.launch {
            val user_id = Firebase.auth.currentUser?.uid.orEmpty()
            getFamily(GetFamilyUseCase.Params(user_id)).collect {
                when (it) {
                    is Result.Error -> {}
                    is Result.Loading -> {}
                    is Result.Success -> activityObserver(
                        ActivityObserver.Params(
                            family_id = it.data.id,
                            child_id = selectedChild.value
                        )
                    )
                }
            }
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
