package com.jsloane.littleone.ui.view.feed.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.R
import com.jsloane.littleone.ui.theme.LittleOneTheme

@Composable
fun AtAGlance(modifier: Modifier = Modifier) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Today at a glance",
                style = MaterialTheme.typography.subtitle1
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlanceItem(
                icon = R.drawable.ic_activity_bottle,
                line1 = "3 feeds",
                line2 = "42 mins"
            )
            GlanceItem(
                icon = R.drawable.ic_diaper,
                line1 = "6 changes",
                line2 = "4 pee · 3 poo"
            )
            GlanceItem(
                icon = R.drawable.ic_activity_sleep,
                line1 = "4 hours"
            )
        }
    }
}

@Composable
fun GlanceItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    line1: String,
    line2: String? = null
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(text = line1, style = MaterialTheme.typography.caption)
                if (line2 != null)
                    Text(text = line2, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAtAGlance() {
    LittleOneTheme {
        AtAGlance(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
    }
}
