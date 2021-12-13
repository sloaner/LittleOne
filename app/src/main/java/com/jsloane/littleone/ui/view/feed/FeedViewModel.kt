package com.jsloane.littleone.ui.view.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.AtAGlanceTimeframe
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.domain.observers.ActivityObserver
import com.jsloane.littleone.domain.observers.ChildObserver
import com.jsloane.littleone.domain.repository.AppSettingsRepository
import com.jsloane.littleone.domain.repository.AppSettingsRepository.Companion.PreferenceKey
import com.jsloane.littleone.domain.usecases.CreateActivityUseCase
import com.jsloane.littleone.domain.usecases.DeleteActivityUseCase
import com.jsloane.littleone.domain.usecases.GetFamilyUseCase
import com.jsloane.littleone.domain.usecases.UpdateActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
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
    private val activityObserver: ActivityObserver,
    private val todayActivityObserver: ActivityObserver
) : ViewModel() {
    private val selectedFilters = MutableStateFlow(listOf<ActivityType>())
    private val currentFamily = MutableStateFlow("invalid")
    private val selectedChild = MutableStateFlow(Child())
    private val glanceTimeframe = MutableStateFlow(AtAGlanceTimeframe.DAY)

    val state: StateFlow<FeedViewState> = combine(
        activityObserver.flow.filter { it is Result.Success },
        todayActivityObserver.flow.filter { it is Result.Success },
        selectedFilters,
        selectedChild
    ) { activities, today, filters, child ->
        FeedViewState(
            activities = (activities as Result.Success<List<Activity>>).data,
            todaysActivities = (today as Result.Success<List<Activity>>).data,
            selectedFilters = filters,
            selectedChild = child,
            timeframe = glanceTimeframe.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedViewState.Empty
    )

    init {
        viewModelScope.launch {
            val user_id = Firebase.auth.currentUser?.uid.orEmpty()
            appSettingsRepository.familyId.collect {
                currentFamily.emit(it)
                childObserver(ChildObserver.Params(family_id = it))
            }
        }
        viewModelScope.launch {
            combine(
                childObserver.flow.filter { it is Result.Success },
                selectedChild,
                glanceTimeframe
            ) { childrenRes, child, timeframe ->
                val savedChildId = appSettingsRepository.childId.first()
                val children = (childrenRes as Result.Success<List<Child>>).data

                val default = children.firstOrNull { it.id == savedChildId }
                    ?: children.firstOrNull()
                    ?: return@combine

                if (default.id != selectedChild.value.id) {
                    selectedChild.emit(default)
                    appSettingsRepository.setPreference(PreferenceKey.CHILD, default.id)
                }

                activityObserver(
                    ActivityObserver.Params(
                        family_id = currentFamily.value,
                        child_id = child.id,
                    )
                )
                todayActivityObserver(
                    ActivityObserver.Params(
                        family_id = currentFamily.value,
                        child_id = child.id,
                        after = when (timeframe) {
                            AtAGlanceTimeframe.DAY -> LocalDate.now()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                            AtAGlanceTimeframe.WEEK -> LocalDate.now()
                                .minusWeeks(1L)
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                            AtAGlanceTimeframe.MONTH -> LocalDate.now()
                                .minusMonths(1L)
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                        }
                    )
                )
            }.collect()
        }
    }

    fun submitAction(action: FeedAction) {
        viewModelScope.launch {
            when (action) {
                is FeedAction.UpdateSelectedFilters -> updateSelectedFilters(action.filter)
                is FeedAction.AddNewActivity -> addNewActivity(action.activity)
                is FeedAction.EditActivity -> editActivity(action.activity)
                is FeedAction.DeleteActivity -> deleteActivity(action.activity)
                is FeedAction.ChangeTimeframe -> glanceTimeframe.emit(action.timeframe)
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
                child_id = selectedChild.value.id,
                activity = activity
            )
        ).last()
    }

    private suspend fun editActivity(activity: Activity) {
        updateActivityUseCase(
            UpdateActivityUseCase.Params(
                family_id = appSettingsRepository.familyId.first(),
                child_id = selectedChild.value.id,
                activity = activity
            )
        ).last()
    }

    private suspend fun deleteActivity(activity: Activity) {
        deleteActivityUseCase(
            DeleteActivityUseCase.Params(
                family_id = appSettingsRepository.familyId.first(),
                child_id = selectedChild.value.id,
                activityId = activity.id
            )
        ).last()
    }
}
