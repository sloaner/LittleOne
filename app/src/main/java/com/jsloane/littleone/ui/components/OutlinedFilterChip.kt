package com.jsloane.littleone.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.R
import com.jsloane.littleone.ui.theme.LittleOneTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OutlinedFilterChip(
    modifier: Modifier = Modifier,
    text: String,
    thumbnailIcon: (@Composable () -> Unit)? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val contentColor = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primarySurface)

    Surface(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        role = Role.Switch,
        color = when (selected) {
            true -> contentColor.copy(alpha = 0.20F)
            false -> Color.Transparent
        },
        border = BorderStroke(
            1.dp,
            contentColor.copy(
                alpha = when {
                    enabled || selected -> ContentAlpha.medium
                    else -> ContentAlpha.disabled
                }
            )
        ),
        shape = RoundedCornerShape(50)
    ) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            if (thumbnailIcon == null && !selected) {
                Spacer(Modifier.size(12.dp))
            } else {
                Box(
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (thumbnailIcon != null) {
                        Surface(
                            color = Color.Transparent,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .alpha(
                                    when {
                                        enabled || selected -> ContentAlpha.high
                                        else -> ContentAlpha.disabled
                                    }
                                )
                        ) {
                            thumbnailIcon()
                        }
                    }
                    if (selected) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp),
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            }
            Text(
                text = text,
                color = contentColor.copy(
                    alpha = when {
                        enabled || selected -> ContentAlpha.high
                        else -> ContentAlpha.disabled
                    }
                ),
                style = typography.body2,
                maxLines = 1,
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, end = 12.dp)
            )
        }
    }
}

@Preview("Day")
@Preview("Night", uiMode = UI_MODE_NIGHT_YES)
@Preview("Large Text", fontScale = 2f)
@Composable
fun FilterChipPreview() {
    LittleOneTheme {
        Surface(color = MaterialTheme.colors.primarySurface) {
            Column {
                OutlinedFilterChip(
                    text = "Disabled Chip",
                    enabled = false,
                    selected = false,
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Enabled Chip",
                    enabled = true,
                    selected = false,
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Selected Chip",
                    enabled = true,
                    selected = true,
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Disabled Image Chip",
                    enabled = false,
                    selected = false,
                    thumbnailIcon = {
                        Box(modifier = Modifier.background(MaterialTheme.colors.secondary))
                    },
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Enabled Image Chip",
                    enabled = true,
                    selected = false,
                    thumbnailIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Selected Image Chip",
                    enabled = true,
                    selected = true,
                    thumbnailIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.img_logo_google),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
