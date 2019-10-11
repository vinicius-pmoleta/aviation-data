package com.aviationdata.core.androidtest.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.aviationdata.core.R

inline fun <reified F : Fragment> launchFragmentTest() {
    launchFragmentInContainer<F>(themeResId = R.style.Theme_AviationData_DayNight)
}