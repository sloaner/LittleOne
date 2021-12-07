package com.jsloane.littleone.ui.view.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.observers.ActivityObserver
import com.jsloane.littleone.domain.observers.ChildObserver
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.usecases.CreateActivityUseCase
import com.jsloane.littleone.domain.usecases.DeleteActivityUseCase
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import com.jsloane.littleone.domain.usecases.UpdateActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val getFamilyUseCase: GetFamilyUseCase,
    private val createActivityUseCase: CreateActivityUseCase,
    private val updateActivityUseCase: UpdateActivityUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val childObserver: ChildObserver,
    private val activityObserver: ActivityObserver
) : ViewModel() {
    private val selectedFilters = MutableStateFlow(listOf<ActivityType>())
    private val selectedChild = MutableStateFlow("FkzZXXvYaIa3QS6IWr2P")

    val state: StateFlow<FeedViewState> = combine(
        activityObserver.flow.filter { it is Result.Success },
        selectedFilters,
        selectedChild
    ) { activities, filters, child ->
        FeedViewState(
            activities = (activities as Result.Success<List<Activity>>).data,
            selectedFilters = filters,
            selectedChild = child
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedViewState.Empty
    )

    init {
        viewModelScope.launch {
            val user_id = Firebase.auth.currentUser?.uid.orEmpty()
            getFamilyUseCase(GetFamilyUseCase.Params(user_id)).collect {
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
                is FeedAction.UpdateSelectedFilters -> updateSelectedFilters(action.filter)
                is FeedAction.AddNewActivity -> addNewActivity(action.activity)
                is FeedAction.EditActivity -> editActivity(action.activity)
                is FeedAction.DeleteActivity -> deleteActivity(action.activity)
                else -> {}
            }
        }
    }

    private suspend fun updateSelectedFilters(filter: ActivityType) {
        val list = selectedFilters.value
        if (list.contains(filter)) {
            selectedFilters.emit(list.filterNot { it == filter })
        } else {
            selectedFilters.emit(list.plus(filter))
        }
    }

    private suspend fun addNewActivity(activity: Activity) {
        createActivityUseCase(
            CreateActivityUseCase.Params(
                family_id = appSettingsRepository.familyId.first(),
                child_id = selectedChild.value,
                activity = activity
            )
        ).last()
    }

    private suspend fun editActivity(activity: Activity) {
        updateActivityUseCase(
            UpdateActivityUseCase.Params(
                family_id = appSettingsRepository.familyId.first(),
                child_id = selectedChild.value,
                activity = activity
            )
        ).last()
    }

    private suspend fun deleteActivity(activity: Activity) {
        deleteActivityUseCase(
            DeleteActivityUseCase.Params(
                family_id = appSettingsRepository.familyId.first(),
                child_id = selectedChild.value,
                activityId = activity.id
            )
        ).last()
    }
}
