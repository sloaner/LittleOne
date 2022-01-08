package com.jsloane.littleone.ui.view.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.ui.view.settings.components.FamilyCard
import com.jsloane.littleone.ui.view.settings.components.GlanceCard
import com.jsloane.littleone.util.rememberFlowWithLifecycle

@Composable
fun SettingsScreen(
    navigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = SettingsViewState.Empty)

    SettingsScreen(
        viewState = viewState,
        actions = {
            when (it) {
                is SettingsAction.NavigateUp -> navigateUp()
                else -> viewModel.submitAction(it)
            }
        }
    )
}

@Composable
private fun SettingsScreen(
    viewState: SettingsViewState,
    actions: (SettingsAction) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    var glanceEnabled by remember { mutableStateOf(true) }
    var slot1: ActivityType.Category? by remember { mutableStateOf(ActivityType.Category.FEEDING) }
    var slot2: ActivityType.Category? by remember { mutableStateOf(null) }
    var slot3: ActivityType.Category? by remember { mutableStateOf(ActivityType.Category.SLEEP) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            SettingsToolbar(
                title = { Text(text = "Settings") },
                onBack = { actions(SettingsAction.NavigateUp) }
            )
        },
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            FamilyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                inviteCode = viewState.family.inviteCode,
                expiration = viewState.family.inviteExpiration,
                refresh = { actions(SettingsAction.RefreshCode) },
                delete = { actions(SettingsAction.DeleteCode) }
            )
            GlanceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                featureEnabled = glanceEnabled,
                toggleFeature = { glanceEnabled = it },
                slot1 = slot1,
                slot2 = slot2,
                slot3 = slot3,
                slot1Changed = { slot1 = it },
                slot2Changed = { slot2 = it },
                slot3Changed = { slot3 = it },
            )
            OutlinedButton(
                modifier = Modifier.padding(16.dp),
                onClick = { actions(SettingsAction.Logout) },
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = "Log Out")
            }
        }
    }
}

@Composable
private fun SettingsToolbar(
    title: @Composable () -> Unit,
    onBack: (() -> Unit)? = null,
) {
    TopAppBar(
        title = title,
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = { onBack.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    LittleOneTheme {
        SettingsScreen(SettingsViewState.Empty) {}
    }
}
