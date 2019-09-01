package com.aviationdata.core.dependencies.modules

import android.content.Context
import com.aviationdata.core.AviationDataApplication
import com.aviationdata.core.structure.AppDispatchers
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

    @Provides
    @Singleton
    fun provideDispatchers(): AppDispatchers {
        return AppDispatchers()
    }
}