package com.jsloane.littleone.ui.view.feed

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.navigation.MainActions
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.ActivityLog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(actions: MainActions? = null) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val selection = remember { mutableStateOf(1) }

    Scaffold(
        scaffoldState = scaffoldState,
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
            scaffoldState = backdropState,
            appBar = {
                TopAppBar(
                    title = { Text("Backdrop scaffold") },
                    navigationIcon = {
                        if (backdropState.isConcealed) {
                            IconButton(onClick = { scope.launch { backdropState.reveal() } }) {
                                Icon(
                                    Icons.Default.Tune,
                                    contentDescription = "Localized description"
                                )
                            }
                        } else {
                            IconButton(onClick = { scope.launch { backdropState.conceal() } }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    },
                    actions = {
                        var clickCount by remember { mutableStateOf(0) }
                        IconButton(
                            onClick = {
                                // show snackbar as a suspend function
                                scope.launch {
                                    Firebase.auth.signOut()
                                    scaffoldState.snackbarHostState.showSnackbar("Signed Out")
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Localized description"
                            )
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
                                scope.launch { backdropState.conceal() }
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    LittleOneTheme {
        FeedScreen()
    }
}
