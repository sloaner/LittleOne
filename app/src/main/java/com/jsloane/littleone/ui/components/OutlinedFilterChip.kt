package com.jsloane.littleone.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.ui.theme.LittleOneTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OutlinedFilterChip(
    text: String,
    thumbnailIcon: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        role = Role.Switch,
        color = when (selected) {
            true -> MaterialTheme.colors.onPrimary.copy(alpha = 0.20F)
            false -> Color.Transparent
        },
        border = BorderStroke(
            1.dp, Color.DarkGray.copy(
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
                Box(modifier = Modifier.padding(start = 4.dp, end = 8.dp), contentAlignment = Alignment.Center) {
                    if (thumbnailIcon != null) {
                        Surface(
                            color = Color.Transparent,
                            modifier = Modifier
                                .matchParentSize()
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
                            modifier = Modifier.size(24.dp).padding(4.dp),
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            }
            Text(
                text = text,
                color = MaterialTheme.colors.onPrimary.copy(
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
        Surface(color = MaterialTheme.colors.primary) {
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
                    text = "Disabled Chip",
                    enabled = false,
                    selected = false,
                    thumbnailIcon = {
                        Box(modifier = Modifier.background(MaterialTheme.colors.secondary))
                    },
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Enabled Chip",
                    enabled = true,
                    selected = false,
                    thumbnailIcon = {
                        Box(modifier = Modifier.background(MaterialTheme.colors.secondary))
                    },
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedFilterChip(
                    text = "Selected Chip",
                    enabled = true,
                    selected = true,
                    thumbnailIcon = {
                        Box(modifier = Modifier.background(MaterialTheme.colors.secondary))
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}