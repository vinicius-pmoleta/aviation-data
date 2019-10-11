package com.aviationdata.common.core.androidtest.robot.common

import com.aviationdata.common.core.R
import com.aviationdata.common.core.androidtest.robot.BaseRobot
import com.aviationdata.common.core.androidtest.waitForView
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed

object LoadingRobot : BaseRobot() {

    fun assertLabelDisplayed(): LoadingRobot {
        waitForView(LoadingRobot.LoadingPage.label) {
            assertDisplayed(LoadingRobot.LoadingPage.label)
            assertContains(
                LoadingRobot.LoadingPage.label,
                R.string.default_loading_label
            )
        }
        return this
    }

    fun assertLabelHidden(): LoadingRobot {
        assertNotDisplayed(LoadingRobot.LoadingPage.label)
        return this
    }

    private object LoadingPage {
        val label = R.id.loading_label
    }
}