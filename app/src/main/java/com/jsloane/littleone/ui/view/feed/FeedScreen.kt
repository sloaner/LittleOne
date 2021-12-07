package com.jsloane.littleone.ui.view.feed

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jsloane.littleone.R
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.feed.components.ActivityLog
import com.jsloane.littleone.ui.view.feed.components.AtAGlance
import com.jsloane.littleone.ui.view.feed.components.FilterPanel
import com.jsloane.littleone.ui.view.feed.components.NewActivityChoiceSheet
import com.jsloane.littleone.ui.view.feed.components.NewActivitySheet
import com.jsloane.littleone.util.rememberFlowWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    openSettings: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = FeedViewState.Empty)

    FeedScreen(
        viewState = viewState,
        actions = {
            when (it) {
                is FeedAction.OpenSettings -> openSettings()
                else -> {
                    viewModel.submitAction(it)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun FeedScreen(
    viewState: FeedViewState,
    actions: (FeedAction) -> Unit
) {
    var selection: ActivityType? by rememberSaveable { mutableStateOf(null) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden) {
        if (it == ModalBottomSheetValue.Hidden) selection = null
        true
    }
    val sheetScrollState = rememberScrollState()

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = {
            TopAppBar(
                title = { Text("Backdrop scaffold") },
                navigationIcon = {
                    Crossfade(targetState = backdropState.isConcealed) {
                        if (it) {
                            IconButton(onClick = { scope.launch { backdropState.reveal() } }) {
                                Icon(
                                    Icons.Default.Tune,
                                    contentDescription = "Show filters"
                                )
                            }
                        } else {
                            IconButton(onClick = { scope.launch { backdropState.conceal() } }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close filters"
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { actions(FeedAction.OpenSettings) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.screen_feed)
                        )
                    }
                },
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            )
        },
        backLayerBackgroundColor = MaterialTheme.colors.primarySurface,
        backLayerContent = {
            FilterPanel(
                filters = viewState.filters,
                filterChanged = {
                    actions(FeedAction.UpdateSelectedFilters(it))
                }
            )
        },
        frontLayerBackgroundColor = MaterialTheme.colors.surface,
        frontLayerContent = {
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetShape = MaterialTheme.shapes.large.copy(
                    bottomStart = ZeroCornerSize,
                    bottomEnd = ZeroCornerSize
                ),
                sheetContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(50)
                                )
                                .size(width = 32.dp, height = 4.dp)
                        )
                        AnimatedVisibility(visible = selection == null) {
                            NewActivityChoiceSheet(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(sheetScrollState)
                            ) { selection = it }
                        }
                        AnimatedVisibility(visible = selection != null) {
                            NewActivitySheet(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(sheetScrollState),
                                activityType = selection ?: ActivityType.LEFT_BREAST
                            ) {
                                actions(FeedAction.AddNewActivity(it))
                                scope.launch {
                                    sheetState.hide()
                                    selection = null
                                }
                            }
                        }
                    }
                }
            ) {
                Scaffold(
                    scaffoldState = scaffoldState,
                    floatingActionButtonPosition = FabPosition.End,
                    backgroundColor = MaterialTheme.colors.surface,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { scope.launch { sheetState.show() } }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                ) {
                    Column {
                        AtAGlance(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                        Divider()
                        ActivityLog(
                            items = viewState.groupedActivities,
                            stopTimer = { actions(FeedAction.EditActivity(it)) },
                            updateItem = { actions(FeedAction.EditActivity(it)) },
                            deleteItem = { actions(FeedAction.DeleteActivity(it)) }
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    LittleOneTheme {
        FeedScreen(FeedViewState.Empty) {}
    }
}
