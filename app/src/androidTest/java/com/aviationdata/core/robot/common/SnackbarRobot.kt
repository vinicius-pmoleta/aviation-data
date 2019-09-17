package com.aviationdata.core.robot.common

import android.widget.Button
import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.aviationdata.core.waitForView
import com.google.android.material.R
import com.schibsted.spain.barista.assertion.BaristaAssertions.assertAny

object SnackbarRobot {

    fun assertText(text: String): SnackbarRobot {
        waitForView(R.id.snackbar_text) {
            assertAny<TextView>(R.id.snackbar_text) { it.text == text }
        }
        return this
    }

    fun assertAction(action: String): SnackbarRobot {
        waitForView(R.id.snackbar_action) {
            assertAny<Button>(R.id.snackbar_action) { it.text == action }
        }
        return this
    }

    fun performAction(): SnackbarRobot {
        waitForView(R.id.snackbar_action) {
            assertAny<Button>(R.id.snackbar_action) { it.performClick() }
        }
        return this
    }
}