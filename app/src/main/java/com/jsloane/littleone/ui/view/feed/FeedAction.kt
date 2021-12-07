package com.jsloane.littleone.ui.view.feed

import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType

sealed class FeedAction {
    object OpenSettings : FeedAction()
    class UpdateSelectedFilters(val filter: ActivityType) : FeedAction()
    class AddNewActivity(val activity: Activity) : FeedAction()
    class EditActivity(val activity: Activity) : FeedAction()
    class DeleteActivity(val activity: Activity) : FeedAction()
}
