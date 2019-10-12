package com.aviationdata.common.core.androidtest.robot.common

import com.aviationdata.common.core.R
import com.aviationdata.common.core.androidtest.robot.BaseRobot
import com.aviationdata.common.core.androidtest.waitForView
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed

object LoadingRobot : BaseRobot() {

    fun assertLabelDisplayed(): LoadingRobot {
        waitForView(LoadingPage.label) {
            assertDisplayed(LoadingPage.label)
            assertContains(
                LoadingPage.label,
                R.string.default_loading_label
            )
        }
        return this
    }

    fun assertLabelHidden(): LoadingRobot {
        assertNotDisplayed(LoadingPage.label)
        return this
    }

    private object LoadingPage {
        val label = R.id.loading_label
    }
}