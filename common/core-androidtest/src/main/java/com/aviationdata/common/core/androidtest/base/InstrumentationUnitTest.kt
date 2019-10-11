package com.aviationdata.common.core.androidtest.base

import androidx.fragment.app.Fragment
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.Before
import org.junit.Rule

abstract class InstrumentationUnitTest {

    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<TestActivity> = activityScenarioRule()

    @Before
    fun before() {
        addFragmentForTesting(provideFragment())
    }

    abstract fun provideFragment(): Fragment

    private fun addFragmentForTesting(fragment: Fragment) {
        activityScenarioRule.scenario.onActivity { activity ->
            val tag = fragment::class.java.name

            activity.supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

}