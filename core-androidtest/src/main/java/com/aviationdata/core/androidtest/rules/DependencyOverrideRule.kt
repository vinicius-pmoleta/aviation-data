package com.aviationdata.core.androidtest.rules

import com.aviationdata.core.dependencies.DependenciesSetup
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class DependencyOverrideRule(
    private val bindings: Kodein.MainBuilder.() -> Unit
) : ExternalResource() {

    init {
        DependenciesSetup.container.addConfig { bindings() }
    }
}