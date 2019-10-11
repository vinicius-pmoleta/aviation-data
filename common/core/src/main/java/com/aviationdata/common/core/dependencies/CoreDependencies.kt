package com.aviationdata.common.core.dependencies

import com.aviationdata.common.core.dependencies.modules.applicationModule
import com.aviationdata.common.core.dependencies.modules.networkModule
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein

val coreModule = Kodein.Module("core") {
    import(applicationModule)
    import(networkModule)
}

val coreComponent = ConfigurableKodein(mutable = true).apply {
    addImport(coreModule)
}