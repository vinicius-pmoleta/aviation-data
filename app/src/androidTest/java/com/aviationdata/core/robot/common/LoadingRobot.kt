package com.aviationdata.core.robot.common

import com.aviationdata.R
import com.aviationdata.core.robot.BaseRobot
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed

object LoadingRobot : BaseRobot() {

    fun assertLabelDisplayed(): LoadingRobot {
        assertDisplayed(LoadingRobot.LoadingPage.label)
        assertContains(LoadingRobot.LoadingPage.label, R.string.default_loading_label)
        return this
    }

    fun assertLabelHidden(): LoadingRobot {
        assertNotDisplayed(LoadingRobot.LoadingPage.label)
        return this
    }

    private object LoadingPage {
        const val label = R.id.loading_label
    }
}