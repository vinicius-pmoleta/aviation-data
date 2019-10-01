package com.aviationdata.core.androidtest.robot

import androidx.annotation.IdRes
import com.aviationdata.core.androidtest.robot.common.SnackbarRobot
import com.aviationdata.core.androidtest.robot.common.ToolbarRobot

open class BaseRobot {

    fun assertToolbarTitle(@IdRes toolbar: Int, text: String): BaseRobot {
        ToolbarRobot.assertTitle(toolbar, text)
        return this
    }

    fun assertSnackbarText(text: String): BaseRobot {
        SnackbarRobot.assertText(text)
        return this
    }

    fun assertSnackbarAction(action: String): BaseRobot {
        SnackbarRobot.assertAction(action)
        return this
    }

    fun performSnackbarAction(): BaseRobot {
        SnackbarRobot.performAction()
        return this
    }
}