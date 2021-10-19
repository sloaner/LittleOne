package com.jsloane.littleone.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import com.google.accompanist.insets.ProvideWindowInsets
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.ActivityLog
import kotlinx.coroutines.launch

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LittleOneApp() {
    ProvideWindowInsets {
        LittleOneTheme {
            val scope = rememberCoroutineScope()
            val appState = rememberAppState()
            val selection = remember { mutableStateOf(1) }

            Scaffold(
                scaffoldState = appState.scaffoldState,
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /* fab click handler */ }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            ) {
                BackdropScaffold(
                    scaffoldState = appState.backdropState,
                    appBar = {
                        TopAppBar(
                            title = { Text("Backdrop scaffold") },
                            navigationIcon = {
                                if (appState.backdropState.isConcealed) {
                                    IconButton(onClick = { scope.launch { appState.backdropState.reveal() } }) {
                                        Icon(Icons.Default.Tune, contentDescription = "Localized description")
                                    }
                                } else {
                                    IconButton(onClick = { scope.launch { appState.backdropState.conceal() } }) {
                                        Icon(Icons.Default.Close, contentDescription = "Localized description")
                                    }
                                }
                            },
                            actions = {
                                var clickCount by remember { mutableStateOf(0) }
                                IconButton(
                                    onClick = {
                                        // show snackbar as a suspend function
                                        scope.launch {
                                            appState.scaffoldState.snackbarHostState
                                                .showSnackbar("Snackbar #${++clickCount}")
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Settings, contentDescription = "Localized description")
                                }
                            },
                            elevation = 0.dp,
                            backgroundColor = Color.Transparent
                        )
                    },
                    backLayerContent = {
                        LazyColumn {
                            items(if (selection.value >= 3) 3 else 5) {
                                ListItem(
                                    Modifier.clickable {
                                        selection.value = it
                                        scope.launch { appState.backdropState.conceal() }
                                    },
                                    text = { Text("Select $it") }
                                )
                            }
                        }
                    },
                    frontLayerContent = {
                        ActivityLog()
                    }
                )
            }
        }
    }
}