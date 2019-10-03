package com.aviationdata.core.dependencies

import com.aviationdata.core.dependencies.modules.applicationModule
import com.aviationdata.core.dependencies.modules.networkModule
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein

val coreModule = Kodein.Module("core") {
    import(applicationModule)
    import(networkModule)
}

val coreComponent = ConfigurableKodein(mutable = true).apply {
    addImport(coreModule)
}