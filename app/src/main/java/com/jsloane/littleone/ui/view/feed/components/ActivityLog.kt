package com.jsloane.littleone.ui.view.feed.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.StopCircle
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.RelativeTimeFormatter
import com.jsloane.littleone.util.toLocalDate
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ActivityLog(
    modifier: Modifier = Modifier,
    items: Map<LocalDate, List<Activity>>,
    stopTimer: (Activity) -> Unit,
    updateItem: (Activity) -> Unit,
    deleteItem: (Activity) -> Unit
) {
    var expanded by remember { mutableStateOf("") }

    LazyColumn(modifier = modifier.padding(vertical = 0.dp, horizontal = 8.dp)) {
        item { Spacer(modifier = Modifier.size(8.dp)) }
        items.forEach { (date, list) ->
            stickyHeader {
                DateHeader(date = date)
            }
            itemsIndexed(list) { index, activity ->
                ActivityItem(
                    expanded = expanded == activity.id,
                    firstItem = index <= 0,
                    lastItem = index >= list.lastIndex,
                    time = activity.start_time,
                    duration = activity.duration,
                    activity = activity.type,
                    description = activity.type.name,
                    onClick = { expanded = if (expanded == activity.id) "" else activity.id },
                    onStop = {
                        updateItem(
                            activity.copy(
                                duration = Duration.between(activity.start_time, Instant.now())
                            )
                        )
                    },
                    onEdit = {},
                    onDelete = { deleteItem(activity) }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ActivityItem(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    firstItem: Boolean = false,
    lastItem: Boolean = false,
    activity: ActivityType,
    time: Instant,
    duration: Duration,
    description: String,
    onClick: () -> Unit,
    onStop: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
            val (dividerRef, iconRef, titleRef, descRef, stopRef, editRef, deleteRef) = createRefs()

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
                        linkTo(
                            start = iconRef.end,
                            startMargin = 8.dp,
                            end = stopRef.start,
                            endMargin = 8.dp,
                            bias = 0f
                        )
                        top.linkTo(parent.top, 9.dp)
                    },
                text = "${activity.category.title} · ${activity.title}",
                style = MaterialTheme.typography.body2
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    modifier = Modifier.constrainAs(descRef) {
                        linkTo(
                            start = titleRef.start,
                            end = stopRef.start,
                            endMargin = 8.dp,
                            bias = 0f
                        )
                        top.linkTo(titleRef.bottom, 2.dp)
                        bottom.linkTo(parent.bottom, 9.dp)
                    },
                    text = RelativeTimeFormatter.format(time),
                    style = MaterialTheme.typography.caption
                )
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(stopRef) {
                    end.linkTo(if (expanded) editRef.start else parent.end)
                    top.linkTo(titleRef.top)
                },
                visible = duration.isZero && activity.features[ActivityType.FEATURE_END],
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onStop) {
                    Icon(imageVector = Icons.Outlined.StopCircle, contentDescription = null)
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(editRef) {
                    end.linkTo(deleteRef.start)
                    top.linkTo(titleRef.top)
                },
                visible = expanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(deleteRef) {
                    end.linkTo(parent.end)
                    top.linkTo(titleRef.top)
                },
                visible = expanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }
            }

            AnimatedVisibility(visible = expanded) {
            }
        }
    }
}

@Composable
fun DateHeader(modifier: Modifier = Modifier, date: LocalDate) {
    Surface(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = RelativeTimeFormatter.format(date),
            style = MaterialTheme.typography.body1
        )
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

@Preview
@Composable
private fun PreviewList() {
    LittleOneTheme {
        ActivityLog(
            items = ActivityType.values()
                .mapIndexed { i, v ->
                    Activity(
                        id = "$i",
                        type = v,
                        start_time = Instant.now().minusSeconds(Random.nextInt(10) * 60L * 56L * 3),
                        duration = Duration.ofMinutes(Random.nextLong(0, 10)),
                        notes = ""
                    )
                }.groupBy {
                    it.start_time.toLocalDate(ZoneId.systemDefault())
                },
            stopTimer = {},
            updateItem = {},
            deleteItem = {}
        )
    }
}
