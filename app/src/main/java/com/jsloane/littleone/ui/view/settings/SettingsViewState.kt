package com.jsloane.littleone.ui.view.settings

import com.jsloane.littleone.domain.model.ActivityType
import com.jsloane.littleone.domain.model.Family

data class SettingsViewState(
    val family: Family = Family("", emptyList(), null, null),
    val glanceEnabled: Boolean = false,
    val glanceSlots: List<ActivityType.Category?> = listOf(null, null, null)
) {
    companion object {
        val Empty = SettingsViewState()
    }
}
