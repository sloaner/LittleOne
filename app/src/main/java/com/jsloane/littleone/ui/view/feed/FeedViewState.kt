package com.jsloane.littleone.ui.view.feed

import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import java.time.LocalDate

data class FeedViewState(
    val selectedChild: String = "",
    val selectedFilters: List<ActivityType> = emptyList(),
    val activities: List<Activity> = emptyList()
) {
    val groupedActivities: Map<LocalDate, List<Activity>> =
        activities.groupBy { it.start_time.toLocalDate() }

    companion object {
        val Empty = FeedViewState()
    }
}
