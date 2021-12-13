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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.R
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.AtAGlanceTimeframe
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.feed.FeedViewState
import com.jsloane.littleone.util.RelativeTimeFormatter
import java.time.Duration

@Composable
fun AtAGlance(
    modifier: Modifier = Modifier,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val feed = items[ActivityType.Category.FEEDING]
            val diaper = items[ActivityType.Category.DIAPER]
            val sleep = items[ActivityType.Category.SLEEP]
            GlanceItem(
                icon = R.drawable.ic_activity_bottle,
                line1 = "${feed?.quantity ?: 0} feeds",
                line2 = RelativeTimeFormatter.formatEstimate(feed?.duration ?: Duration.ZERO)
            )
            GlanceItem(
                icon = R.drawable.ic_diaper,
                line1 = "${diaper?.quantity ?: 0} changes",
                line2 = "${diaper?.splitQuantity?.get(ActivityType.PEE) ?: 0} pee · " +
                    "${diaper?.splitQuantity?.get(ActivityType.POOP) ?: 0} poo"
            )
            GlanceItem(
                icon = R.drawable.ic_activity_sleep,
                line1 = RelativeTimeFormatter.formatEstimate(sleep?.duration ?: Duration.ZERO)
            )
        }
    }
}

@Composable
fun GlanceItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    line1: String,
    line2: String? = null
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(text = line1, style = MaterialTheme.typography.caption)
                if (line2 != null)
                    Text(text = line2, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAtAGlance() {
    LittleOneTheme {
        AtAGlance(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
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
