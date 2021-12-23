package com.jsloane.littleone.ui.view.onboard

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.DateVisualTransformation
import com.jsloane.littleone.util.Formatters
import com.jsloane.littleone.util.rememberFlowWithLifecycle
import java.time.Instant
import java.time.LocalDate

@Composable
fun OnboardingScreen(
    inviteCode: String? = null,
    viewModel: OnboardViewModel = hiltViewModel()
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = OnboardViewState.Empty)
    val scope = rememberCoroutineScope()

    OnboardingScreen(
        viewState = viewState,
        actions = { viewModel.submitAction(it) }
    )
}

private enum class OnboardState {
    ROOT, NEW, JOIN
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun OnboardingScreen(
    viewState: OnboardViewState,
    actions: (OnboardAction) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedScreen by remember { mutableStateOf(OnboardState.ROOT) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = "let's get started".lowercase().replace(' ', '\n'),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h2
        )

        ExpandingButton(
            modifier = Modifier.fillMaxWidth(),
            contentVisible = selectedScreen == OnboardState.NEW,
            onClick = { selectedScreen = OnboardState.NEW },
            buttonLabel = {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Start a new family"
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Let's add a child to your family",
                    style = MaterialTheme.typography.body2
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Baby's name") },
                    value = viewState.baby_name,
                    onValueChange = { actions(OnboardAction.UpdateBabyName(it)) },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Birthday") },
                    value = viewState.baby_birthday,
                    onValueChange = { actions(OnboardAction.UpdateBabyBirthday(it)) },
                    placeholder = { Text("MM/DD/YYYY") },
                    singleLine = true,
                    visualTransformation = DateVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val picker = MaterialDatePicker.Builder
                                    .datePicker()
                                    .setTitleText("Select Birthday")
                                    .build()
                                picker.show(
                                    (context as AppCompatActivity).supportFragmentManager,
                                    picker.toString()
                                )
                                picker.addOnPositiveButtonClickListener { millis ->
                                    actions(
                                        OnboardAction.UpdateBabyBirthday(
                                            Formatters.squishedDate.format(
                                                Instant.ofEpochMilli(millis)
                                            )
                                        )
                                    )
                                }
                            }
                        ) {
                            Icon(Icons.Default.Today, "")
                        }
                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        actions(
                            OnboardAction.CreateFamily(
                                viewState.baby_name,
                                LocalDate.parse(viewState.baby_birthday, Formatters.squishedDate)
                            )
                        )
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Continue"
                    )
                }
            }
        }

        ExpandingButton(
            modifier = Modifier.fillMaxWidth(),
            contentVisible = selectedScreen == OnboardState.JOIN,
            onClick = { selectedScreen = OnboardState.JOIN },
            buttonLabel = {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Join an existing family"
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Invite Code") },
                    value = viewState.invite_code,
                    onValueChange = { actions(OnboardAction.UpdateInviteCode(it)) },
                    singleLine = true
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(50),
                    onClick = { actions(OnboardAction.JoinFamily(viewState.invite_code)) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Continue"
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
fun ExpandingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonLabel: @Composable ColumnScope.() -> Unit,
    buttonStyle: TextStyle = MaterialTheme.typography.button,
    headerStyle: TextStyle = MaterialTheme.typography.h6,
    contentVisible: Boolean,
    contentColor: Color = MaterialTheme.colors.onSurface,
    content: @Composable() ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colors.surface)
            .clickable {
                onClick()
            }
            .padding(8.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalContentAlpha provides contentColor.alpha
        ) {
            val styleProgress: Float by animateFloatAsState(if (contentVisible) 1f else 0f)

            ProvideTextStyle(value = lerp(buttonStyle, headerStyle, styleProgress)) {
                buttonLabel()
            }

            AnimatedVisibility(visible = contentVisible) {
                content()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun Preview() {
    LittleOneTheme {
        OnboardingScreen(OnboardViewState.Empty, {})
    }
}
