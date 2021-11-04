package com.jsloane.littleone.ui.view.onboard

import java.time.LocalDate

sealed class OnboardAction {
    object OpenActivityLog : OnboardAction()
    class CreateFamily(val childName: String, val childBirthday: LocalDate) : OnboardAction()
    class JoinFamily(val inviteCode: String) : OnboardAction()
}
