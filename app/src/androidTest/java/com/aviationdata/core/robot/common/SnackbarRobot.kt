package com.aviationdata.core.robot.common

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
}