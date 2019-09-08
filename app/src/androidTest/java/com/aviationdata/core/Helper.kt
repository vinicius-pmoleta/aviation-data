package com.aviationdata.core

import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry

fun application() =
    InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .applicationContext as AviationDataApplication

fun resources(): Resources =
    InstrumentationRegistry
        .getInstrumentation()
        .targetContext
        .resources