package com.aviationdata.core.androidtest.robot.common

import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import com.schibsted.spain.barista.assertion.BaristaAssertions.assertAny

object ToolbarRobot {

    fun assertTitle(@IdRes toolbar: Int, text: String): ToolbarRobot {
        assertAny<Toolbar>(toolbar) { it.title == text }
        return this
    }
}