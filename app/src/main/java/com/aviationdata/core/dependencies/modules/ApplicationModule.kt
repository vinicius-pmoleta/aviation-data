package com.aviationdata.core.dependencies.modules

import android.content.Context
import com.aviationdata.core.AviationDataApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AviationDataApplication) {

    @Provides
    @Singleton
    fun providesApplication(): AviationDataApplication {
        return application
    }

    @Provides
    @Singleton
    fun providesContext(): Context {
        return application.applicationContext
    }
}