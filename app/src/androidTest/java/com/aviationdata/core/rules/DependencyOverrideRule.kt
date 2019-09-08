package com.aviationdata.core.rules

import com.aviationdata.core.application
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class DependencyOverrideRule(
    private val bindings: Kodein.MainBuilder.() -> Unit
) : ExternalResource() {

    init {
        val container = application().kodein
        container.addConfig { bindings() }
    }
}