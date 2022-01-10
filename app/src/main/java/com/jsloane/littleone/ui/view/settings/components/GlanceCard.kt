package com.jsloane.littleone.ui.view.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.components.DragContainer
import com.jsloane.littleone.ui.components.DragReceiver
import com.jsloane.littleone.ui.components.Draggable
import com.jsloane.littleone.ui.components.dashedBorder
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.feed.FeedViewState
import com.jsloane.littleone.ui.view.feed.components.GlanceItem
import java.time.Duration

@Composable
fun GlanceCard(
    modifier: Modifier = Modifier,
    featureEnabled: Boolean = true,
    toggleFeature: (Boolean) -> Unit = {},
    slot1: ActivityType.Category? = null,
    slot2: ActivityType.Category? = null,
    slot3: ActivityType.Category? = null,
    slot1Changed: (ActivityType.Category?) -> Unit = {},
    slot2Changed: (ActivityType.Category?) -> Unit = {},
    slot3Changed: (ActivityType.Category?) -> Unit = {}
) {
    Card(modifier = modifier) {
        DragContainer(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "At A Glance",
                        style = MaterialTheme.typography.body1
                    )
                    Switch(
                        checked = featureEnabled,
                        onCheckedChange = toggleFeature,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.primary
                        )
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Selected",
                    style = MaterialTheme.typography.body2
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DragReceiver(
                        modifier = Modifier.weight(1f),
                        onReceive = slot1Changed
                    ) { receiving ->
                        GlanceSlot(
                            highlight = receiving,
                            onRemove = { slot1Changed(null) },
                            content = slot1?.let {
                                { BorderedGlanceItem(highlight = receiving, category = it) }
                            }
                        )
                    }
                    DragReceiver(
                        modifier = Modifier.weight(1f),
                        onReceive = slot2Changed
                    ) { receiving ->
                        GlanceSlot(
                            highlight = receiving,
                            onRemove = { slot2Changed(null) },
                            content = slot2?.let {
                                { BorderedGlanceItem(highlight = receiving, category = it) }
                            }
                        )
                    }
                    DragReceiver(
                        modifier = Modifier.weight(1f),
                        onReceive = slot3Changed
                    ) { receiving ->
                        GlanceSlot(
                            highlight = receiving,
                            onRemove = { slot3Changed(null) },
                            content = slot3?.let {
                                { BorderedGlanceItem(highlight = receiving, category = it) }
                            }
                        )
                    }
                }
                Divider(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Available",
                    style = MaterialTheme.typography.body2
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp
                ) {
                    listOf(
                        ActivityType.Category.FEEDING,
                        ActivityType.Category.SLEEP,
                        ActivityType.Category.PLAY,
                        ActivityType.Category.LEISURE,
                        ActivityType.Category.DIAPER
                    ).forEach { category ->
                        Column {
                            Text(category.title, style = MaterialTheme.typography.caption)
                            Draggable(dragDataProducer = { category }) {
                                BorderedGlanceItem(category = category)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlanceSlot(
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onRemove: () -> Unit,
    content: (@Composable () -> Unit)?
) {
    Box {
        if (content == null) {
            val alpha = if (highlight) ContentAlpha.high else .12f
            CompositionLocalProvider(LocalContentAlpha provides alpha) {
                Box(
                    modifier = modifier
                        .defaultMinSize(minWidth = 96.dp, minHeight = 64.dp)
                        .dashedBorder(
                            width = 2.dp,
                            color = MaterialTheme.colors.onSurface.copy(alpha),
                            shape = MaterialTheme.shapes.small,
                            on = 4.dp,
                            off = 4.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Empty",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        } else {
            Box(
                modifier = modifier.defaultMinSize(minHeight = 64.dp),
                propagateMinConstraints = true
            ) {
                content()
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = -8.dp)
                    .size(32.dp)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable { onRemove() }
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun BorderedGlanceItem(
    modifier: Modifier = Modifier,
    category: ActivityType.Category,
    highlight: Boolean = false
) {
    val alpha = if (highlight) 1f else .12f
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 64.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = alpha),
                shape = MaterialTheme.shapes.small
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        GlanceItem(
            category = category,
            data = FeedViewState.AggregateActivity(
                quantity = 6,
                duration = Duration.ofMinutes(42),
                splitQuantity = mapOf(ActivityType.PEE to 4, ActivityType.POOP to 3)
            )
        )
    }
}

@Preview
@Composable
private fun PreviewGlanceCard() {
    LittleOneTheme {
        GlanceCard(
            slot1 = ActivityType.Category.DIAPER,
            slot2 = null,
            slot3 = ActivityType.Category.DIAPER
        )
    }
}

@Preview
@Composable
private fun PreviewDragTarget() {
    LittleOneTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlanceSlot(onRemove = {}) {
                BorderedGlanceItem(category = ActivityType.Category.FEEDING)
            }
            GlanceSlot(onRemove = {}, content = null)
            GlanceSlot(onRemove = {}) {
                BorderedGlanceItem(category = ActivityType.Category.SLEEP)
            }
        }
    }
}
