package com.jsloane.littleone.ui.view.login

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jsloane.littleone.BuildConfig
import com.jsloane.littleone.R
import com.jsloane.littleone.navigation.MainActions
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.activity

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    actions: MainActions? = null,
    viewModel: LoginViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val loginState = viewModel.loadingState.collectAsState()
    val context = LocalContext.current

    if (loginState.value == LoginViewModel.LoadingState.SUCCESS) {
        actions?.gotoFeed?.invoke()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithCredential(credential)
        } catch (e: Exception) {
            Log.w("TAG", "Google sign in failed", e)
        }
    }

    Surface {
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

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
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
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        viewModel.signInWithEmailAndPassword(
                            viewModel.email,
                            viewModel.password
                        )
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
                onClick = {
                    viewModel.signInWithEmailAndPassword(
                        viewModel.email,
                        viewModel.password
                    )
                }
            ) {
                Text(text = "Login", style = MaterialTheme.typography.h6)
            }
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
                    onClick = {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                            .requestEmail()
                            .build()

                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
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
                    onClick = {
                        val pendingResultTask = Firebase.auth.pendingAuthResult
                        if (pendingResultTask != null) {
                            pendingResultTask.addOnSuccessListener {
                                actions?.gotoFeed?.invoke()
                            }.addOnFailureListener {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            context.activity?.let { activity ->
                                Firebase.auth.startActivityForSignInWithProvider(
                                    activity,
                                    OAuthProvider.newBuilder(GithubAuthProvider.PROVIDER_ID)
                                        .setScopes(listOf("user:email"))
                                        .build()
                                ).addOnSuccessListener {
                                    actions?.gotoFeed?.invoke()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
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

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun Preview() {
    LittleOneTheme {
        LoginScreen(null, LoginViewModel())
    }
}
