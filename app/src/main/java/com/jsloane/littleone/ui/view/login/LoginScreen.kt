package com.jsloane.littleone.ui.view.login

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.BuildConfig
import com.jsloane.littleone.R
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.activity
import com.jsloane.littleone.util.rememberFlowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    openActivityLog: () -> Unit,
    openOnboarding: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = LoginViewState.Empty)

    LoginScreen(
        viewState = viewState,
        actions = {
            when (it) {
                is LoginAction.OpenActivityLog -> {
                    Log.d("TAG", "Heading to activity!")
                    openActivityLog()
                }
                is LoginAction.OpenOnboarding -> {
                    Log.d("TAG", "Heading to onboarding!")
                    openOnboarding()
                }
                else -> {
                    Log.d("TAG", "Handoff to view model")
                    viewModel.submitAction(it)
                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoginScreen(
    viewState: LoginViewState,
    actions: (LoginAction) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    if (viewState.pendingNavigation != null) {
        Log.d("TAG", "Pending nav")
        actions(viewState.pendingNavigation)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { intent ->
                actions(LoginAction.SignInToken(intent))
            }
        }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 60.dp),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h2
            )

            val (focusRequester) = FocusRequester.createRefs()
            val keyboardController = LocalSoftwareKeyboardController.current

            //region Login Form
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = viewState.email,
                onValueChange = { actions(LoginAction.UpdateEmail(it)) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester.requestFocus() }
                )
            )
            PasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .focusRequester(focusRequester),
                value = viewState.password,
                onValueChange = { actions(LoginAction.UpdatePassword(it)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        actions(LoginAction.SignInEmail())
                    }
                )
            )

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 24.dp),
                onClick = {}
            ) {
                Text("Forgot Password")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                onClick = {
                    actions(LoginAction.SignInEmail())
                }
            ) {
                Text(text = "Login", style = MaterialTheme.typography.h6)
            }
            //endregion

            //region Register Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don’t have an account?",
                    style = MaterialTheme.typography.body2
                )
                TextButton(
                    onClick = {}
                ) {
                    Text(text = "Sign up")
                }
            }
            //endregion

            //region 3rd Party Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(MaterialTheme.colors.onSurface)
                        .weight(1f)
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "or login with",
                    style = MaterialTheme.typography.body2
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .background(MaterialTheme.colors.onSurface)
                        .weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThirdPartyLoginButton(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth(1f)
                        .weight(1f),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        launcher.launch(
                            GoogleSignIn.getClient(
                                context,
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                                    .requestEmail()
                                    .build()
                            ).signInIntent
                        )
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_logo_google),
                        contentDescription = null
                    )
                }
                ThirdPartyLoginButton(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth(1f)
                        .weight(1f),
                    shape = RoundedCornerShape(50),
                    onClick = {
                        val pendingResultTask = Firebase.auth.pendingAuthResult
                        if (pendingResultTask != null) {
                            pendingResultTask.addOnSuccessListener {
                                actions(LoginAction.OpenActivityLog)
                            }.addOnFailureListener {
                                showErrorSnackbar(
                                    snackbarHostState = scaffoldState.snackbarHostState,
                                    scope = scope,
                                    throwable = it
                                )
                            }
                        } else {
                            context.activity?.let { activity ->
                                Firebase.auth.startActivityForSignInWithProvider(
                                    activity,
                                    OAuthProvider.newBuilder(GithubAuthProvider.PROVIDER_ID)
                                        .setScopes(listOf("user:email"))
                                        .build()
                                ).addOnSuccessListener {
                                    actions(LoginAction.OpenActivityLog)
                                }.addOnFailureListener {
                                    showErrorSnackbar(
                                        snackbarHostState = scaffoldState.snackbarHostState,
                                        scope = scope,
                                        throwable = it
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_logo_github),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                    )
                }
            }
            //endregion
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = { Text("Password") },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = true,
        visualTransformation = when (passwordVisibility) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = {
            IconButton(
                onClick = { passwordVisibility = !passwordVisibility }
            ) {
                val icon = when (passwordVisibility) {
                    true -> Icons.Default.Visibility
                    false -> Icons.Default.VisibilityOff
                }
                Icon(icon, "")
            }
        }
    )
}

fun showErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    throwable: Throwable = Throwable(),
    onActionPerformed: () -> Unit = {},
    onDismissed: () -> Unit = {}
) {
    scope.launch {
        when (
            snackbarHostState.showSnackbar(
                message = throwable.message ?: "Unable to login",
                actionLabel = "Retry"
            )
        ) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> onDismissed()
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun Preview() {
    LittleOneTheme {
        LoginScreen(LoginViewState.Empty) {}
    }
}
