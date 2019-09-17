package com.aviationdata.core

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.junit.Assert.assertNotNull
import java.util.concurrent.TimeUnit

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

fun Any.readFile(path: String) =
    this.javaClass
        .classLoader!!
        .getResourceAsStream(path)
        .bufferedReader()
        .use { it.readText() }

fun waitForView(@IdRes id: Int, actions: () -> Unit) {
    waitForView(withId(id), actions)
}

fun waitForView(matcher: Matcher<View>, actions: () -> Unit) {
    val waitTime = TimeUnit.SECONDS.toMillis(5)
    val endTime = System.currentTimeMillis() + waitTime
    do {
        try {
            onView(matcher).check { view, _ -> assertNotNull(view) }
            actions()
            return
        } catch (ignored: Exception) {
            // Ignoring exceptions to continue trying until timeout
        } catch (ignored: Error) {
            // Ignoring errors to continue trying until timeout
        }
    } while (System.currentTimeMillis() < endTime)
}