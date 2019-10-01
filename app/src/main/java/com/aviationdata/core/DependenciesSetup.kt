package com.aviationdata.core

import android.app.Application
import com.aviationdata.core.dependencies.modules.applicationModule
import com.aviationdata.core.dependencies.modules.networkModule
import com.aviationdata.search.searchModule
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class DependenciesSetup(private val application: Application) {

    val container by lazy {
        ConfigurableKodein(mutable = true).apply {
            modules.forEach { addImport(it) }
            addConfig {
                bind() from singleton { application }
            }
        }
    }

    private val modules = listOf(
        applicationModule,
        networkModule,
        searchModule
    )
}