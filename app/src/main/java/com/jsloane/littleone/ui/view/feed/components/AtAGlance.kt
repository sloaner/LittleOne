package com.jsloane.littleone.ui.view.feed.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jsloane.littleone.R

@Composable
fun AtAGlance(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (titleRef, pickerRef, eatRef, diaperRef, sleepRef) = createRefs()

        Text(
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 24.dp)
            },
            text = "Today at a glance"
        )
        Icon(
            modifier = Modifier.constrainAs(pickerRef) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 24.dp)
            },
            imageVector = Icons.Default.Event,
            contentDescription = null
        )
        GlanceItem(
            modifier = Modifier.constrainAs(eatRef) {
                top.linkTo(titleRef.bottom)
                linkTo(start = titleRef.start, end = diaperRef.start)
            },
            icon = R.drawable.ic_bottle,
            line1 = "3 feeds",
            line2 = "42 mins"
        )
        GlanceItem(
            modifier = Modifier.constrainAs(diaperRef) {
                top.linkTo(titleRef.bottom)
                linkTo(start = eatRef.end, end = sleepRef.start)

            },
            icon = R.drawable.ic_diaper,
            line1 = "6 changes",
            line2 = "4 pee · 3 poo"
        )
        GlanceItem(
            modifier = Modifier.constrainAs(sleepRef) {
                top.linkTo(titleRef.bottom)
                linkTo(start = diaperRef.end, end = pickerRef.end)

            },
            icon = R.drawable.ic_sleep,
            line1 = "4 hours"
        )
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
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Icon(painter = painterResource(id = icon), contentDescription = null)
            Column {
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
    MaterialTheme {
        AtAGlance(modifier = Modifier.fillMaxWidth())
    }
}