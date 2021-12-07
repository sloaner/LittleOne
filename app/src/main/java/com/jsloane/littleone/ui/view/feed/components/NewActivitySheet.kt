package com.jsloane.littleone.ui.view.feed.components

import android.content.Context
import android.text.format.DateUtils.DAY_IN_MILLIS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.toLocalDate
import com.jsloane.littleone.util.toLocalTime
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d")
private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

@Composable
fun NewActivityChoiceSheet(
    modifier: Modifier = Modifier,
    onClick: (ActivityType) -> Unit
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(bottom = 16.dp)
        ) {
            NewActivitySheetHeader(header = ActivityType.Category.FEEDING)
            NewActivitySheetRow(label = ActivityType.RIGHT_BREAST, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.LEFT_BREAST, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.BOTTLE, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.MEAL, onClick = onClick)

            NewActivitySheetHeader(header = ActivityType.Category.DIAPER)
            NewActivitySheetRow(label = ActivityType.PEE, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.POOP, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.BOTH, onClick = onClick)

            NewActivitySheetHeader(header = ActivityType.Category.LEISURE)
            NewActivitySheetRow(label = ActivityType.TUMMY_TIME, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.PLAY, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.OUTDOORS, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.BATH, onClick = onClick)
            NewActivitySheetRow(label = ActivityType.TV, onClick = onClick)

            NewActivitySheetHeader(header = ActivityType.Category.SLEEP)
            NewActivitySheetRow(label = ActivityType.SLEEP, onClick = onClick)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewActivitySheet(
    modifier: Modifier = Modifier,
    activityType: ActivityType,
    startDateTime: Instant = Instant.now(),
    onSubmit: (Activity) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var date by remember { mutableStateOf(startDateTime.toLocalDate(ZoneId.systemDefault())) }
    var startTime by remember { mutableStateOf(startDateTime.toLocalTime(ZoneId.systemDefault())) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }
    var quantity by remember { mutableStateOf(0f) }
    var notes: String by remember { mutableStateOf("") }

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = activityType.title,
                    style = MaterialTheme.typography.body1
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focus ->
                        if (focus.isFocused || focus.hasFocus) {
                            showDatePicker(
                                context = context,
                                titleText = "Select Date",
                                date = date.toEpochDay() * DAY_IN_MILLIS,
                                onSuccess = { date = LocalDate.ofEpochDay(it / DAY_IN_MILLIS) },
                                onDismiss = { focusManager.clearFocus() }
                            )
                        }
                    },
                value = dateFormatter.format(date),
                onValueChange = { },
                label = { Text("Date") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Today,
                        contentDescription = "choose date"
                    )
                },
                singleLine = true,
                readOnly = true
            )
            val startLabel =
                if (activityType.features[ActivityType.FEATURE_END]) "Start Time" else "Time"
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focus ->
                        if (focus.isFocused || focus.hasFocus) {
                            showTimePicker(
                                context = context,
                                titleText = "Select $startLabel",
                                hour = startTime.hour,
                                minute = startTime.minute,
                                onSuccess = { startTime = LocalTime.of(it.hour, it.minute) },
                                onDismiss = { focusManager.clearFocus() }
                            )
                        }
                    },
                value = timeFormatter.format(startTime),
                onValueChange = { },
                label = { Text(startLabel) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null
                    )
                },
                singleLine = true,
                readOnly = true
            )
            if (activityType.features[ActivityType.FEATURE_END]) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focus ->
                            if (focus.hasFocus || focus.isFocused) {
                                showTimePicker(
                                    context = context,
                                    titleText = "Select Finish Time",
                                    hour = endTime?.hour ?: startTime.plusMinutes(15).hour,
                                    minute = endTime?.minute ?: startTime.plusMinutes(15).minute,
                                    onSuccess = { endTime = LocalTime.of(it.hour, it.minute) },
                                    onDismiss = { focusManager.clearFocus() }
                                )
                            }
                        },
                    value = endTime?.let { timeFormatter.format(it) } ?: "",
                    onValueChange = { },
                    label = { Text("Finish Time (Optional)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (endTime != null) {
                            IconButton(onClick = { endTime = null }) {
                                Icon(
                                    modifier = Modifier.scale(0.75f),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "remove finish time"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    readOnly = true
                )
            }
            if (activityType.features[ActivityType.FEATURE_QUANTITY]) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Slider(
                        modifier = Modifier.fillMaxWidth(.8f),
                        value = quantity,
                        onValueChange = { quantity = it },
                        valueRange = 0f..10f
                    )
                    Text(text = "%.1f oz".format(quantity))
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                maxLines = 3
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                onClick = {
                    onSubmit(
                        Activity(
                            id = "",
                            type = activityType,
                            start_time = LocalDateTime
                                .of(date, startTime)
                                .atZone(ZoneId.systemDefault())
                                .toInstant(),
                            duration =
                            if (endTime != null) {
                                Duration.between(startTime, endTime)
                            } else {
                                Duration.ZERO
                            },
                            quantity = quantity,
                            notes = notes
                        )
                    )
                }
            ) {
                if (endTime != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        Text(text = "Save", style = MaterialTheme.typography.h6)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Text(text = "Start", style = MaterialTheme.typography.h6)
                    }
                }
            }
        }
    }
}

private fun showDatePicker(
    context: Context,
    titleText: String,
    date: Long,
    onSuccess: (Long) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    (context as? FragmentActivity)?.let { activity ->
        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(titleText)
            .setSelection(date)
            .build()
            .apply {
                addOnPositiveButtonClickListener { millis ->
                    onSuccess(millis)
                }
                addOnDismissListener {
                    onDismiss()
                }
                show(
                    activity.supportFragmentManager,
                    "materialDatePicker"
                )
            }
    }
}

private fun showTimePicker(
    context: Context,
    titleText: String,
    hour: Int,
    minute: Int,
    onSuccess: (MaterialTimePicker) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    (context as? FragmentActivity)?.let { activity ->
        MaterialTimePicker
            .Builder()
            .setTitleText(titleText)
            .setHour(hour)
            .setMinute(minute)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    onSuccess(this)
                }
                addOnDismissListener {
                    onDismiss()
                }
                show(
                    activity.supportFragmentManager,
                    "materialTimePicker"
                )
            }
    }
}

@Composable
private fun NewActivitySheetHeader(
    modifier: Modifier = Modifier,
    header: ActivityType.Category
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        Text(
            modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp),
            text = header.title,
            style = MaterialTheme.typography.body1
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NewActivitySheetRow(
    modifier: Modifier = Modifier,
    label: ActivityType,
    onClick: (ActivityType) -> Unit
) {
    Surface(modifier = modifier.fillMaxWidth(), onClick = { onClick(label) }) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                painterResource(id = label.icon),
                null,
                tint = MaterialTheme.colors.onSurface
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = label.title,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview
@Composable
private fun previewNewActivitySheet() {
    LittleOneTheme {
        Column {
            NewActivitySheet(
                modifier = Modifier.fillMaxWidth(),
                activityType = ActivityType.LEFT_BREAST
            ) {}
            Divider()
            NewActivitySheet(
                modifier = Modifier.fillMaxWidth(),
                activityType = ActivityType.POOP
            ) {}
        }
    }
}

@Preview
@Composable
private fun previewNewActivityChoiceSheet() {
    LittleOneTheme {
        NewActivityChoiceSheet(Modifier.fillMaxWidth()) {}
    }
}
