package com.aviationdata.core.rules

import androidx.test.platform.app.InstrumentationRegistry
import com.aviationdata.core.AviationDataApplication
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class DependencyOverrideRule(
    private val bindings: Kodein.MainBuilder.() -> Unit
) : ExternalResource() {

    init {
        val container = application().kodein
        container.addConfig { bindings() }
    }

    private fun application() =
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as AviationDataApplication
}