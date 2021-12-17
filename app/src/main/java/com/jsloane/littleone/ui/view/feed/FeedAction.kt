package com.jsloane.littleone.ui.view.feed

import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.AtAGlanceTimeframe

sealed class FeedAction {
    object OpenSettings : FeedAction()
    object OpenLogin : FeedAction()
    class UpdateSelectedFilters(val filter: ActivityType) : FeedAction()
    class AddNewActivity(val activity: Activity) : FeedAction()
    class EditActivity(val activity: Activity) : FeedAction()
    class DeleteActivity(val activity: Activity) : FeedAction()
    class ChangeTimeframe(val timeframe: AtAGlanceTimeframe) : FeedAction()
}
