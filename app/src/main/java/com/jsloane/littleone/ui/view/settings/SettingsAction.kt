package com.jsloane.littleone.ui.view.settings

sealed class SettingsAction {
    object NavigateUp : SettingsAction()
    object Logout : SettingsAction()
    object RefreshCode : SettingsAction()
    object ShareCode : SettingsAction()
    object DeleteCode : SettingsAction()
}
