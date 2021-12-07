package com.jsloane.littleone.ui.view.feed

import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.view.feed.components.ActivityFilterState

data class FeedViewState(
    val selectedChild: String = "",
    val selectedFilters: List<ActivityType> = emptyList(),
    val activities: List<Activity> = emptyList()
) {
    val filteredActivities = activities.filter {
        selectedFilters.isEmpty() || selectedFilters.contains(it.type)
    }

    val groupedActivities = filteredActivities.groupBy { it.start_time.toLocalDate() }

    val filters = filterGroups.mapValues { entry ->
        entry.value.map { type ->
            ActivityFilterState(type, selectedFilters.contains(type))
        }
    }

    companion object {
        val Empty = FeedViewState()
    }
}

private val filterGroups = sortedMapOf(
    ActivityType.Category.FEEDING to listOf(
        ActivityType.LEFT_BREAST,
        ActivityType.RIGHT_BREAST,
        ActivityType.BOTTLE,
        ActivityType.MEAL,
    ),
    ActivityType.Category.DIAPER to listOf(
        ActivityType.PEE,
        ActivityType.POOP,
        ActivityType.BOTH,
    ),
    ActivityType.Category.LEISURE to listOf(
        ActivityType.TUMMY_TIME,
        ActivityType.PLAY,
        ActivityType.OUTDOORS,
        ActivityType.BATH,
        ActivityType.TV,
    ),
    ActivityType.Category.SLEEP to listOf(
        ActivityType.SLEEP
    )
)
