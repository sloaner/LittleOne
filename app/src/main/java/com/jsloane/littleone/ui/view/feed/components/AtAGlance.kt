package com.jsloane.littleone.ui.view.feed.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.AtAGlanceTimeframe
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.feed.FeedViewState
import com.jsloane.littleone.util.RelativeTimeFormatter
import java.time.Duration

@Composable
fun AtAGlance(
    modifier: Modifier = Modifier,
    slots: List<ActivityType.Category?>,
    items: Map<ActivityType.Category, FeedViewState.AggregateActivity> = emptyMap(),
    timeframe: AtAGlanceTimeframe,
    changeTimeframe: (AtAGlanceTimeframe) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (timeframe) {
                    AtAGlanceTimeframe.DAY -> "Today at a glance"
                    AtAGlanceTimeframe.WEEK -> "This week at a glance"
                    AtAGlanceTimeframe.MONTH -> "This month at a glance"
                },
                style = MaterialTheme.typography.subtitle1
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
        fun onClick(timeframe: AtAGlanceTimeframe) {
            changeTimeframe(timeframe)
            expanded = false
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = { onClick(AtAGlanceTimeframe.DAY) }) { Text("Today") }
            DropdownMenuItem(onClick = { onClick(AtAGlanceTimeframe.WEEK) }) { Text("Last 7 days") }
            DropdownMenuItem(onClick = { onClick(AtAGlanceTimeframe.MONTH) }) { Text("Last month") }
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = when (slots.count { it != null }) {
                1 -> Arrangement.Center
                2 -> Arrangement.SpaceAround
                else -> Arrangement.SpaceBetween
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            slots.filterNotNull().forEach {
                GlanceItem(category = it, data = items[it] ?: FeedViewState.AggregateActivity())
            }
        }
    }
}

@Composable
fun GlanceItem(
    modifier: Modifier = Modifier,
    category: ActivityType.Category,
    data: FeedViewState.AggregateActivity
) {
    when (category) {
        ActivityType.Category.FEEDING -> FeedingSlot(
            quantity = data.quantity,
            duration = data.duration
        )
        ActivityType.Category.DIAPER -> DiaperSlot(
            quantity = data.quantity,
            pee = data.splitQuantity[ActivityType.PEE] ?: 0,
            poo = data.splitQuantity[ActivityType.POOP] ?: 0
        )
        ActivityType.Category.SLEEP -> SleepSlot(
            duration = data.duration
        )
        ActivityType.Category.LEISURE -> LeisureSlot(
            duration = data.duration
        )
        ActivityType.Category.PLAY -> PlaySlot(
            duration = data.duration
        )
    }
}

@Composable
private fun GlanceItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    line1: String,
    line2: String? = null
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = line1,
                    style = MaterialTheme.typography.caption,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (line2 != null)
                    Text(
                        text = line2,
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
            }
        }
    }
}

@Composable
private fun FeedingSlot(quantity: Int, duration: Duration) {
    GlanceItem(
        icon = ActivityType.Category.FEEDING.icon,
        line1 = "$quantity feeds",
        line2 = RelativeTimeFormatter.formatEstimate(duration)
    )
}

@Composable
private fun DiaperSlot(quantity: Int, pee: Int, poo: Int) {
    GlanceItem(
        icon = ActivityType.Category.DIAPER.icon,
        line1 = "$quantity changes",
        line2 = "$pee pee · $poo poo"
    )
}

@Composable
private fun SleepSlot(duration: Duration) {
    GlanceItem(
        icon = ActivityType.Category.SLEEP.icon,
        line1 = RelativeTimeFormatter.formatEstimate(duration)
    )
}

@Composable
private fun LeisureSlot(duration: Duration) {
    GlanceItem(
        icon = ActivityType.Category.LEISURE.icon,
        line1 = RelativeTimeFormatter.formatEstimate(duration)
    )
}

@Composable
private fun PlaySlot(duration: Duration) {
    GlanceItem(
        icon = ActivityType.Category.PLAY.icon,
        line1 = RelativeTimeFormatter.formatEstimate(duration)
    )
}

@Preview
@Composable
private fun PreviewAtAGlance() {
    LittleOneTheme {
        AtAGlance(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            slots = listOf(
                ActivityType.Category.FEEDING,
                ActivityType.Category.DIAPER,
                ActivityType.Category.SLEEP
            ),
            items = mapOf(
                ActivityType.Category.FEEDING to FeedViewState.AggregateActivity(
                    quantity = 3,
                    duration = Duration.ofMinutes(42L)
                ),
                ActivityType.Category.DIAPER to FeedViewState.AggregateActivity(
                    quantity = 6,
                    splitQuantity = mapOf(
                        ActivityType.PEE to 4,
                        ActivityType.POOP to 3
                    )
                ),
                ActivityType.Category.SLEEP to FeedViewState.AggregateActivity(
                    duration = Duration.ofHours(4L)
                )
            ),
            timeframe = AtAGlanceTimeframe.DAY
        ) {}
    }
}
