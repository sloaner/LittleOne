package com.jsloane.littleone.ui.view.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.R
import com.jsloane.littleone.navigation.MainActions
import com.jsloane.littleone.ui.theme.LittleOneTheme

@Composable
fun LoginScreen(actions: MainActions? = null) {
    Surface {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 60.dp),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h2
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = "",
                onValueChange = {},
                label = { Text("Email") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = "",
                onValueChange = {},
                label = { Text("Password") }
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
                onClick = { actions?.gotoFeed?.invoke() }
            ) {
                Text(text = "Login")
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
                        .weight(1f)
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
                        .weight(1f)
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

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun Preview() {
    LittleOneTheme {
        LoginScreen()
    }
}
