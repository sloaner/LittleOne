package com.jsloane.littleone.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.jsloane.littleone.base.Result
import com.jsloane.littleone.domain.usecases.GetActivityUseCase
import com.jsloane.littleone.domain.usecases.UpdateActivityUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getActivityUseCase: GetActivityUseCase

    @Inject
    lateinit var updateActivityUseCase: UpdateActivityUseCase

    @Inject
    lateinit var workManager: WorkManager

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            STOP_TIMER -> {
                val familyId = intent.getStringExtra(TimerWorker.FAMILY_ID_KEY).orEmpty()
                val childId = intent.getStringExtra(TimerWorker.CHILD_ID_KEY).orEmpty()
                val activityId = intent.getStringExtra(TimerWorker.ACTIVITY_ID_KEY).orEmpty()

                goAsync {
                    getActivityUseCase(
                        GetActivityUseCase.Params(
                            familyId,
                            childId,
                            activityId
                        )
                    ).collectLatest {
                        when (it) {
                            is Result.Loading -> {}
                            is Result.Error -> {}
                            is Result.Success -> {
                                val activity = it.data
                                if (activity.isTimerRunning) {
                                    updateActivityUseCase(
                                        UpdateActivityUseCase.Params(
                                            familyId,
                                            childId,
                                            activity.copy(duration = activity.durationSinceStart())
                                        )
                                    ).collectLatest {
                                        println("DONE")
                                    }
                                }
                            }
                        }
                    }
                }
                println("TIMER STOPPED")
            }
        }
    }

    companion object {
        const val STOP_TIMER = "com.jsloane.littleone.broadcast.STOP_TIMER"
    }
}
