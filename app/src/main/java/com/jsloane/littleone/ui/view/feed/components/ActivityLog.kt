package com.jsloane.littleone.ui.view.feed.components

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.DisposableEffect
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
import com.jsloane.littleone.util.Formatters
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
    var now by remember { mutableStateOf(Instant.now()) }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                now = Instant.now()
                handler.postDelayed(this, 60_000)
            }
        }

        handler.postDelayed(runnable, 60_000)
        onDispose { handler.removeCallbacks(runnable) }
    }

    LazyColumn(modifier = modifier.padding(vertical = 0.dp, horizontal = 8.dp)) {
        item { Spacer(modifier = Modifier.size(8.dp)) }
        items.forEach { (date, list) ->
            stickyHeader {
                DateHeader(date = date)
            }
            itemsIndexed(list) { index, activity ->
                ActivityItem(
                    duration = activity.duration,
                    time = activity.start_time,
                    now = now,
                    activity = activity.type,
                    description = activity.notes,
                    isExpanded = expanded == activity.id,
                    isFirstItem = index <= 0,
                    isLastItem = index >= list.lastIndex,
                    onClick = { expanded = if (expanded == activity.id) "" else activity.id },
                    onStop = {
                        stopTimer(
                            activity.copy(
                                duration = Duration.between(activity.start_time, Instant.now())
                            )
                        )
                    },
                    onEdit = {
                        updateItem(activity)
                        expanded = ""
                    },
                    onDelete = {
                        deleteItem(activity)
                        expanded = ""
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ActivityItem(
    modifier: Modifier = Modifier,
    activity: ActivityType,
    time: Instant = Instant.now(),
    now: Instant = Instant.now(),
    duration: Duration = Duration.ZERO,
    description: String = "",
    isExpanded: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    onClick: () -> Unit,
    onStop: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .defaultMinSize(minHeight = 42.dp)
        ) {
            val (
                timelineBarRef,
                timelineIconRef,
                titleRef,
                timeAgoRef,
                timerRef,
                descRef,
                stopButtonRef,
                editButtonRef,
                deleteButtonRef
            ) = createRefs()

            val isTimerRunning = duration.isZero && activity.features[ActivityType.FEATURE_END]
            var timer by remember { mutableStateOf(now) }

            if (isTimerRunning) {
                DisposableEffect(Unit) {
                    val handler = Handler(Looper.getMainLooper())
                    val runnable = object : Runnable {
                        override fun run() {
                            timer = Instant.now()
                            handler.postDelayed(this, 1_000)
                        }
                    }

                    handler.postDelayed(runnable, 1_000)
                    onDispose { handler.removeCallbacks(runnable) }
                }
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colors.secondary)
                    .constrainAs(timelineBarRef) {
                        top.linkTo(if (isFirstItem) timelineIconRef.bottom else parent.top)
                        bottom.linkTo(if (isLastItem) timelineIconRef.top else parent.bottom)
                        height = Dimension.fillToConstraints

                        centerHorizontallyTo(timelineIconRef)
                    }
            )

            TimelineMarker(
                modifier = Modifier
                    .constrainAs(timelineIconRef) {
                        linkTo(top = titleRef.top, bottom = timeAgoRef.bottom)
                        start.linkTo(parent.start)
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
                            start = timelineIconRef.end,
                            startMargin = 8.dp,
                            end = stopButtonRef.start,
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
                    modifier = Modifier.constrainAs(timeAgoRef) {
                        linkTo(
                            start = titleRef.start,
                            startMargin = 0.dp,
                            top = titleRef.bottom,
                            topMargin = 2.dp,
                            end = stopButtonRef.start,
                            endMargin = 8.dp,
                            bottom = parent.bottom,
                            bottomMargin = 9.dp,
                            horizontalBias = 0f,
                            verticalBias = 0f
                        )
                    },
                    text = if (isExpanded) {
                        Formatters.time_month_day_year.format(time)
                    } else {
                        RelativeTimeFormatter.format(time) +
                                if (isTimerRunning) ""
                                else " • ${RelativeTimeFormatter.formatTimerShort(duration)}"
                    },
                    style = MaterialTheme.typography.caption
                )
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(timerRef) {
                    top.linkTo(stopButtonRef.top)
                    end.linkTo(stopButtonRef.start)
                    bottom.linkTo(stopButtonRef.bottom)
                },
                visible = isTimerRunning,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Text(
                    text = RelativeTimeFormatter.formatTimer(Duration.between(time, timer)),
                    style = MaterialTheme.typography.subtitle1
                )
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(stopButtonRef) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                visible = isTimerRunning,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onStop) {
                    Icon(imageVector = Icons.Outlined.StopCircle, contentDescription = null)
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(editButtonRef) {
                    top.linkTo(stopButtonRef.bottom)
                    end.linkTo(deleteButtonRef.start)
                },
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(deleteButtonRef) {
                    top.linkTo(stopButtonRef.bottom)
                    end.linkTo(parent.end)
                },
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(descRef) {
                    start.linkTo(timeAgoRef.start)
                    top.linkTo(timeAgoRef.bottom, 8.dp)
                    end.linkTo(if (isExpanded) editButtonRef.start else parent.end)
                    width = Dimension.fillToConstraints
                },
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!duration.isZero) {
                            Text(
                                text = "Duration: ${RelativeTimeFormatter.format(duration)}",
                                style = MaterialTheme.typography.caption
                            )
                        }
                        Text(
                            text = description,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
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
                        notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                    )
                }
                .sortedByDescending { it.start_time }
                .groupBy { it.start_time.toLocalDate(ZoneId.systemDefault()) },
            stopTimer = {},
            updateItem = {},
            deleteItem = {}
        )
    }
}
