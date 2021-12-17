package com.jsloane.littleone.ui.view.settings

import com.jsloane.littleone.domain.model.Activity
import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.AtAGlanceTimeframe

sealed class SettingsAction {
    object OpenSettings : SettingsAction()
    object OpenLogin : SettingsAction()
    class UpdateSelectedFilters(val filter: ActivityType) : SettingsAction()
    class AddNewActivity(val activity: Activity) : SettingsAction()
    class EditActivity(val activity: Activity) : SettingsAction()
    class DeleteActivity(val activity: Activity) : SettingsAction()
    class ChangeTimeframe(val timeframe: AtAGlanceTimeframe) : SettingsAction()
}
