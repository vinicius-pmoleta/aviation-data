package com.aviationdata.core

import android.app.Application
import org.kodein.di.KodeinAware

class AviationDataApplication : Application(), KodeinAware {

    override val kodein = DependenciesSetup(this).container

}