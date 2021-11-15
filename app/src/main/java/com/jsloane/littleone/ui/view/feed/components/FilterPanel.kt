package com.jsloane.littleone.ui.view.feed.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.jsloane.littleone.ui.components.ChipGroup
import com.jsloane.littleone.ui.components.OutlinedFilterChip
import com.jsloane.littleone.ui.theme.LittleOneTheme
import kotlin.random.Random

@Composable
fun FilterPanel(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = MaterialTheme.colors.contentColorFor(backgroundColor)
) {
    Column {
        FilterGroup(
            header = "Feeding",
            filters = mapOf(
                ActivityType.LEFT_BREAST to Random.nextBoolean(),
                ActivityType.RIGHT_BREAST to Random.nextBoolean(),
                ActivityType.BOTTLE to Random.nextBoolean(),
                ActivityType.MEAL to Random.nextBoolean()
            )
        )
        FilterGroup(
            header = "Diapering",
            filters = mapOf(
                ActivityType.PEE to Random.nextBoolean(),
                ActivityType.POOP to Random.nextBoolean()
            )
        )
        FilterGroup(
            header = "Sleep",
            filters = mapOf(
                ActivityType.SLEEP to Random.nextBoolean()
            )
        )
        FilterGroup(
            header = "Leisure",
            filters = mapOf(
                ActivityType.TUMMY_TIME to Random.nextBoolean(),
                ActivityType.PLAY to Random.nextBoolean(),
                ActivityType.OUTDOORS to Random.nextBoolean(),
                ActivityType.BATH to Random.nextBoolean(),
                ActivityType.TV to Random.nextBoolean()
            )
        )
    }
}

@Composable
fun FilterGroup(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = MaterialTheme.colors.contentColorFor(backgroundColor),
    header: String?,
    filters: Map<ActivityType, Boolean>
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
                    OutlinedFilterChip(text = it.key.title, selected = it.value)
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
                FilterPanel()
            },
            frontLayerBackgroundColor = MaterialTheme.colors.surface,
            frontLayerContent = {}
        )
    }
}
