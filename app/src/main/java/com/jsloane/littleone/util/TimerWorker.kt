package com.jsloane.littleone.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.jsloane.littleone.MainActivity
import com.jsloane.littleone.R
import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.usecases.GetActivityUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class TimerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    private val getActivityUseCase: GetActivityUseCase
) : CoroutineWorker(context, parameters) {

    private val notificationManager: NotificationManager? = context.getSystemService()

    private val familyId = parameters.inputData.getString(FAMILY_ID_KEY).orEmpty()
    private val childId = parameters.inputData.getString(CHILD_ID_KEY).orEmpty()
    private val activityId = parameters.inputData.getString(ACTIVITY_ID_KEY).orEmpty()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    override suspend fun doWork(): Result {
        setForeground(updateForegroundInfo(null))
        var isComplete = false
        var errorCount = 0
        var result: Result? = null

        while (!isComplete) {
            getActivityUseCase(GetActivityUseCase.Params(familyId, childId, activityId)).collect {
                when (it) {
                    is com.jsloane.littleone.base.Result.Loading -> {}
                    is com.jsloane.littleone.base.Result.Error -> {
                        if (errorCount++ >= 3) {
                            result = Result.failure()
                            isComplete = true
                            delay(30_000)
                        }
                    }
                    is com.jsloane.littleone.base.Result.Success -> {
                        if (!it.data.isTimerRunning) {
                            result = Result.success()
                            isComplete = true
                        } else {
                            setForeground(updateForegroundInfo(it.data))
                            val duration = it.data.durationSinceStart()
                            if (duration.toMinutes() < 60L) {
                                delay(30_000)
                            } else {
                                delay(30 * 60_000)
                            }
                        }
                    }
                }
            }
        }
        return result ?: Result.failure()
    }

    private fun updateForegroundInfo(activity: Activity?): ForegroundInfo {
        val tapAction = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val stopAction = PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(applicationContext, NotificationBroadcastReceiver::class.java).apply {
                action = NotificationBroadcastReceiver.STOP_TIMER
                putExtra(FAMILY_ID_KEY, familyId)
                putExtra(CHILD_ID_KEY, childId)
                putExtra(ACTIVITY_ID_KEY, activityId)
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("${activity?.type?.category?.title ?: "Activity"} in Progress")
            .setContentText(
                activity?.let {
                    "${it.type.title} · " +
                        RelativeTimeFormatter.formatTimerShort(it.durationSinceStart())
                } ?: ""
            )
            .setGroup(GROUP_KEY)
            .setContentIntent(tapAction)
            .addAction(
                R.drawable.ic_notification_icon,
                "End ${activity?.type?.category?.title ?: "Activity"}",
                stopAction
            )
            .build()

        return ForegroundInfo(activityId.hashCode(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = applicationContext.getString(R.string.channel_name)
        val description = applicationContext.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_LOW

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
        }

        notificationManager?.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "timer_channel"
        private const val GROUP_KEY = "com.jsloane.littleone.TIMER_GROUP"

        const val FAMILY_ID_KEY = "family_id"
        const val CHILD_ID_KEY = "child_id"
        const val ACTIVITY_ID_KEY = "activity_id"
    }
}
