package com.aviationdata.core.dependencies

import android.app.Application
import com.aviationdata.core.dependencies.modules.applicationModule
import com.aviationdata.core.dependencies.modules.networkModule
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

object DependenciesSetup {

    val container by lazy {
        ConfigurableKodein(mutable = true).apply {
            modules.forEach { addImport(it) }
        }
    }

    fun initialize(application: Application): DependenciesSetup {
        container.addConfig {
            bind() from singleton { application }
        }
        return this
    }

    private val modules = listOf(
        applicationModule,
        networkModule
    )
}