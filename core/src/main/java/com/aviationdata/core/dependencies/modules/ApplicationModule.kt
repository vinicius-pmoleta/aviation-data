package com.aviationdata.core.dependencies.modules

import com.aviationdata.core.structure.AppDispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val applicationModule = Kodein.Module("application") {

    bind() from singleton {
        AppDispatchers()
    }
}