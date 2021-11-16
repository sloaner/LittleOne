package com.jsloane.littleone.ui.view.feed.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.RelativeTimeFormatter
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.random.Random

val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ActivityItem(
    modifier: Modifier = Modifier,
    lastItem: Boolean = false,
    activity: ActivityType,
    time: Instant,
    duration: Duration,
    description: String
) {
    var expanded by remember { mutableStateOf(false) }
    val elevationProgress: Float by animateFloatAsState(if (expanded) 2f else 0f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = elevationProgress.dp),
        onClick = { expanded = !expanded },
        elevation = elevationProgress.dp
    ) {
        ConstraintLayout(modifier = Modifier.padding(vertical = 2.dp, horizontal = 16.dp)) {
            val (timeRef, iconRef, descRef, dividerRef) = createRefs()
            createHorizontalChain(timeRef, iconRef, descRef, chainStyle = ChainStyle.Packed)

            Text(
                modifier = Modifier.constrainAs(timeRef) {
                    linkTo(start = parent.start, end = iconRef.start, bias = 0f)

                    centerVerticallyTo(iconRef)
                },
                text = timeFormatter.format(time).lowercase(),
                style = MaterialTheme.typography.caption
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colors.secondary,
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .constrainAs(iconRef) {
                        top.linkTo(parent.top)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(id = activity.icon), null)
            }
            AnimatedVisibility(
                visible = !lastItem && !expanded,
                modifier = Modifier.constrainAs(dividerRef) {
                    top.linkTo(iconRef.bottom, 4.dp)
                    centerHorizontallyTo(iconRef)
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(1.dp, 8.dp)
                        .background(color = MaterialTheme.colors.secondary)
                )
            }
            Text(
                modifier = Modifier.constrainAs(descRef) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(iconRef)
                },
                text = "${duration.toMinutes()}m, $description",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ActivityItemConnected(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    firstItem: Boolean = false,
    lastItem: Boolean = false,
    activity: ActivityType,
    time: Instant,
    duration: Duration,
    description: String,
    onClick: () -> Unit
) {
    val elevationProgress: Float by animateFloatAsState(if (expanded) 2f else 0f)
    val borderProgress by animateColorAsState(
        if (expanded)
            MaterialTheme.colors.onSurface.copy(alpha = 1f)
        else
            MaterialTheme.colors.onSurface.copy(alpha = 0f)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 0.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .defaultMinSize(minHeight = 42.dp)
        ) {
            val (titleRef, iconRef, descRef, dividerRef) = createRefs()

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colors.secondary)
                    .constrainAs(dividerRef) {
                        top.linkTo(if (firstItem) iconRef.bottom else parent.top)
                        bottom.linkTo(if (lastItem) iconRef.top else parent.bottom)
                        height = Dimension.fillToConstraints

                        centerHorizontallyTo(iconRef)
                    }
            )

            TimelineMarker(
                modifier = Modifier
                    .constrainAs(iconRef) {
                        start.linkTo(parent.start)
                        linkTo(titleRef.top, descRef.bottom)
                    }
            ) {
                Icon(
                    painterResource(id = activity.icon),
                    null,
                    tint = MaterialTheme.colors.onSecondary
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(titleRef) {
                        start.linkTo(iconRef.end, 8.dp)
                        top.linkTo(parent.top, 9.dp)
                    },
                text = "${activity.category} · ${activity.title}",
                style = MaterialTheme.typography.body2
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    modifier = Modifier.constrainAs(descRef) {
                        start.linkTo(titleRef.start)
                        top.linkTo(titleRef.bottom, 2.dp)
                        bottom.linkTo(parent.bottom, 9.dp)
                    },
                    text = RelativeTimeFormatter.format(time),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Composable
fun TimelineMarker(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(color = MaterialTheme.colors.secondary, shape = CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActivityLog(modifier: Modifier = Modifier) {
    var expanded: Int by remember { mutableStateOf(3) }

    LazyColumn(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        itemsIndexed(ActivityType.values()) { index, activityType ->

            ActivityItemConnected(
                expanded = expanded == index,
                firstItem = index <= 0,
                lastItem = index >= ActivityType.values().lastIndex,
                time = Instant.now().minusSeconds(index * 60L * 56L * 3),
                duration = Duration.ofMinutes(Random.nextLong(1, 20)),
                activity = activityType,
                description = activityType.name,
                onClick = { expanded = if (expanded == index) -1 else index }
            )
        }
    }
}

@Preview
@Composable
fun previewList() {
    LittleOneTheme {
        ActivityLog()
    }
}
