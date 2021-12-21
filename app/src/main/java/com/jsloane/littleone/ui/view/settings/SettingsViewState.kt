package com.jsloane.littleone.ui.view.settings

import com.jsloane.littleone.domain.model.Family

data class SettingsViewState(
    val family: Family = Family("", emptyList(), null, null)
) {
    companion object {
        val Empty = SettingsViewState()
    }
}
