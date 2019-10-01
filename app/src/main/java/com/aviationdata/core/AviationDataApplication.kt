package com.aviationdata.core

import android.app.Application
import com.aviationdata.core.dependencies.DependenciesSetup
import org.kodein.di.KodeinAware

class AviationDataApplication : Application(), KodeinAware {

    override val kodein = DependenciesSetup.initialize(this).container

}