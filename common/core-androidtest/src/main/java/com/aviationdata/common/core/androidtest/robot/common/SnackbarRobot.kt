package com.aviationdata.common.core.androidtest.robot.common

import android.widget.Button
import android.widget.TextView
import com.aviationdata.common.core.androidtest.waitForView
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