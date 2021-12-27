package com.jsloane.littleone.ui.view.feed

import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.AtAGlanceTimeframe
import com.jsloane.littleone.domain.model.Child
import com.jsloane.littleone.ui.view.feed.components.ActivityFilterState
import com.jsloane.littleone.util.equalsAny
import com.jsloane.littleone.util.toLocalDate
import java.time.Duration
import java.time.ZoneId

data class FeedViewState(
    val selectedChild: Child = Child(),
    val selectedFilters: List<ActivityType> = emptyList(),
    val activities: List<Activity> = emptyList(),
    val todaysActivities: List<Activity> = emptyList(),
    val timeframe: AtAGlanceTimeframe = AtAGlanceTimeframe.DAY,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false
) {
    val filteredActivities = activities.filter {
        selectedFilters.isEmpty() || selectedFilters.contains(it.type)
    }

    val groupedActivities = filteredActivities.groupBy {
        it.start_time.toLocalDate(ZoneId.systemDefault())
    }

    val glanceActivities = todaysActivities
        .groupingBy { it.type.category }
        .fold(AggregateActivity()) { a, e -> a + e }

    val filters = filterGroups.mapValues { entry ->
        entry.value.map { type ->
            ActivityFilterState(type, selectedFilters.contains(type))
        }
    }

    companion object {
        val Empty = FeedViewState()
    }

    data class AggregateActivity(
        val quantity: Int = 0,
        val duration: Duration = Duration.ZERO,
        val splitQuantity: Map<ActivityType, Int> = mapOf(
            ActivityType.PEE to 0,
            ActivityType.POOP to 0
        )
    ) {
        operator fun plus(other: Activity): AggregateActivity {
            return this.copy(
                quantity = this.quantity + 1,
                duration = this.duration + other.duration,
                splitQuantity = this.splitQuantity.mapValues {
                    if (other.type.equalsAny(it.key, ActivityType.BOTH)) it.value + 1 else it.value
                }
            )
        }
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
