package com.aviationdata.core.robot.common

import android.widget.Button
import android.widget.TextView
import com.google.android.material.R
import com.schibsted.spain.barista.assertion.BaristaAssertions.assertAny

object SnackbarRobot {

    fun assertText(text: String): SnackbarRobot {
        assertAny<TextView>(R.id.snackbar_text) {
            it.text == text
        }
        return this
    }

    fun assertAction(action: String): SnackbarRobot {
        assertAny<Button>(R.id.snackbar_action) {
            it.text == action
        }
        return this
    }

    fun performAction(): SnackbarRobot {
        assertAny<Button>(R.id.snackbar_action) {
            it.performClick()
        }
        return this
    }
}