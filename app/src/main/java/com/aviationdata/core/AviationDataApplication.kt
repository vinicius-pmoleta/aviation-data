package com.aviationdata.core

import android.app.Application
import com.aviationdata.core.dependencies.DependenciesSetup
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class AviationDataApplication : Application(), KodeinAware {

    override val kodein: Kodein = DependenciesSetup(this).container

}