package com.aviationdata.core.robot

import androidx.annotation.IdRes
import com.aviationdata.core.robot.common.SnackbarRobot
import com.aviationdata.core.robot.common.ToolbarRobot

open class BaseRobot {

    fun assertToolbarTitle(@IdRes toolbar: Int, text: String) {
        ToolbarRobot.assertTitle(toolbar, text)
    }

    fun assertSnackbarText(text: String) {
        SnackbarRobot.assertText(text)
    }
}