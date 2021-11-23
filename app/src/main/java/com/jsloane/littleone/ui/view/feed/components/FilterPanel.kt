package com.jsloane.littleone.ui.view.feed.components

import android.content.res.Configuration
import android.provider.SyncStateContract
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.components.ChipGroup
import com.jsloane.littleone.ui.components.OutlinedFilterChip
import com.jsloane.littleone.ui.theme.LittleOneTheme

data class FilterState(val type: ActivityType, var selected: Boolean = false)

@Composable
fun FilterPanel(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = MaterialTheme.colors.contentColorFor(backgroundColor),
    filters: List<ActivityType>,
    filtersChanged: (List<ActivityType>) -> Unit
) {

    val groupedFilters = remember {
        mutableStateMapOf<ActivityType.Category, List<FilterState>>().apply {
            filters
                .map { FilterState(it, false) }
                .groupBy { it.type.category }
                .also {
                    putAll(it)
                }
        }
    }

    LazyColumn(modifier = modifier) {
        items(groupedFilters.keys.sortedBy { it.order }.toList()) { group ->
            FilterGroup(
                header = group.name,
                filters = groupedFilters[group]?.sortedBy { it.type.order }.orEmpty(),
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                onClick = { type ->
                    val index = groupedFilters[group]?.indexOfFirst { it.type == type } ?: 0
                    groupedFilters[group] = groupedFilters[group]!!.toMutableList().apply {
                        this[index].selected = !this[index].selected
                    }
                    filtersChanged(
                        groupedFilters
                            .flatMap { it.value }
                            .filter { it.selected }
                            .map { it.type }
                    )
                }
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
    filters: List<FilterState>,
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
                FilterPanel(filters = ActivityType.values().toList(), filtersChanged = {})
            },
            frontLayerBackgroundColor = MaterialTheme.colors.surface,
            frontLayerContent = {}
        )
    }
}
