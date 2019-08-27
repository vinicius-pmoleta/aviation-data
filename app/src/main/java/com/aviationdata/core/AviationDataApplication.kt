package com.aviationdata.core

import android.app.Application
import com.aviationdata.core.dependencies.ApplicationComponent
import com.aviationdata.core.dependencies.DaggerApplicationComponent
import com.aviationdata.core.dependencies.modules.ApplicationModule
import com.aviationdata.core.dependencies.modules.NetworkModule

class AviationDataApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initializeDependencyInjection()
    }

    private fun initializeDependencyInjection() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .build()
    }
}