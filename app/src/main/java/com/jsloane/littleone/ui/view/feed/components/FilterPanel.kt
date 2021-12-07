package com.jsloane.littleone.ui.view.feed.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.components.ChipGroup
import com.jsloane.littleone.ui.components.OutlinedFilterChip
import com.jsloane.littleone.ui.theme.LittleOneTheme

data class ActivityFilterState(val type: ActivityType, var selected: Boolean = false)

@Composable
fun FilterPanel(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = MaterialTheme.colors.contentColorFor(backgroundColor),
    filters: Map<ActivityType.Category, List<ActivityFilterState>>,
    filterChanged: (ActivityType) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(filters.keys.toList()) { group ->
            FilterGroup(
                header = group.name,
                filters = filters[group].orEmpty(),
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                onClick = { filterChanged(it) }
            )
        }
    }
}

@Composable
fun FilterGroup(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = MaterialTheme.colors.contentColorFor(backgroundColor),
    header: String?,
    filters: List<ActivityFilterState>,
    onClick: (ActivityType) -> Unit
) {
    Column {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Divider(
                modifier = Modifier.padding(bottom = 16.dp),
                color = contentColor.copy(alpha = 0.12F),
                startIndent = 20.dp
            )

            header?.let {
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    Text(
                        text = it
                    )
                }
            }

            ChipGroup(
                modifier = Modifier
                    .padding(start = 20.dp, end = 10.dp, bottom = 16.dp)
            ) {
                filters.forEach {
                    OutlinedFilterChip(
                        text = it.type.title,
                        selected = it.selected,
                        onClick = { onClick(it.type) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview("Day")
@Preview("Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPanel() {
    LittleOneTheme {
        val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
        BackdropScaffold(
            scaffoldState = scaffoldState,
            appBar = {
                TopAppBar(
                    title = { Text("Backdrop scaffold") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                        }
                    },
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                )
            },
            backLayerBackgroundColor = MaterialTheme.colors.primarySurface,
            backLayerContent = {
                FilterPanel(
                    filters = sortedMapOf(
                        ActivityType.Category.FEEDING to listOf(
                            ActivityFilterState(ActivityType.LEFT_BREAST, false),
                            ActivityFilterState(ActivityType.RIGHT_BREAST, false),
                            ActivityFilterState(ActivityType.BOTTLE, false),
                            ActivityFilterState(ActivityType.MEAL, false),
                        ),
                        ActivityType.Category.DIAPER to listOf(
                            ActivityFilterState(ActivityType.PEE, false),
                            ActivityFilterState(ActivityType.POOP, false),
                            ActivityFilterState(ActivityType.BOTH, false),
                        ),
                        ActivityType.Category.LEISURE to listOf(
                            ActivityFilterState(ActivityType.TUMMY_TIME, false),
                            ActivityFilterState(ActivityType.PLAY, false),
                            ActivityFilterState(ActivityType.OUTDOORS, false),
                            ActivityFilterState(ActivityType.BATH, false),
                            ActivityFilterState(ActivityType.TV, false),
                        ),
                        ActivityType.Category.SLEEP to listOf(
                            ActivityFilterState(ActivityType.SLEEP, false)
                        )
                    ),
                    filterChanged = {}
                )
            },
            frontLayerBackgroundColor = MaterialTheme.colors.surface,
            frontLayerContent = {}
        )
    }
}
