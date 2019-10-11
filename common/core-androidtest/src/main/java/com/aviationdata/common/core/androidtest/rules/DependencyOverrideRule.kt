package com.aviationdata.common.core.androidtest.rules

import org.junit.rules.ExternalResource
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein

class DependencyOverrideRule(
    component: ConfigurableKodein,
    bindings: Kodein.MainBuilder.() -> Unit
) : ExternalResource() {

    init {
        component.addConfig { bindings() }
    }
}