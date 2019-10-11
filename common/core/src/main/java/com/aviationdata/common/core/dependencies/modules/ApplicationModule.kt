package com.aviationdata.common.core.dependencies.modules

import com.aviationdata.common.core.structure.AppDispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val applicationModule = Kodein.Module("application") {

    bind() from singleton {
        AppDispatchers()
    }
}