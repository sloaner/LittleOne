package com.jsloane.littleone.ui.view.settings

import com.jsloane.littleone.domain.model.ActivityType

sealed class SettingsAction {
    object NavigateUp : SettingsAction()
    object Logout : SettingsAction()
    object RefreshCode : SettingsAction()
    object ShareCode : SettingsAction()
    object DeleteCode : SettingsAction()
    class UpdateGlanceEnabled(val enabled: Boolean) : SettingsAction()
    class UpdateGlanceSlot1(val category: ActivityType.Category?) : SettingsAction()
    class UpdateGlanceSlot2(val category: ActivityType.Category?) : SettingsAction()
    class UpdateGlanceSlot3(val category: ActivityType.Category?) : SettingsAction()
}
