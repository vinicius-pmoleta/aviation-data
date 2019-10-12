package com.aviationdata.common.core.androidtest.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.aviationdata.common.core.R

inline fun <reified F : Fragment> launchFragmentTest() {
    launchFragmentInContainer<F>(themeResId = R.style.Theme_AviationData_DayNight)
}

inline fun <reified F : Fragment> launchParameterizedFragmentTest(arguments: Bundle) {
    launchFragmentInContainer<F>(
        themeResId = R.style.Theme_AviationData_DayNight,
        fragmentArgs = arguments
    )
}