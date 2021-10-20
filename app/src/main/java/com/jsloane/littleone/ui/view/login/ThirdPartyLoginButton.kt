package com.jsloane.littleone.ui.view.login

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.R
import com.jsloane.littleone.ui.theme.LittleOneTheme

@Composable
fun ThirdPartyLoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    OutlinedButton(
        modifier = Modifier
            .requiredSizeIn(minHeight = 24.dp, minWidth = 24.dp)
            .then(modifier),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(8.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        content()
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    LittleOneTheme {
        Surface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThirdPartyLoginButton(
                    Modifier
                        .height(32.dp)
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
                        .height(32.dp)
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
