package com.aviationdata.core.dependencies

import com.aviationdata.core.AviationDataApplication
import com.aviationdata.core.dependencies.modules.ApplicationModule
import com.aviationdata.core.dependencies.modules.NetworkModule
import com.aviationdata.core.structure.AppDispatchers
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    fun application(): AviationDataApplication

    fun retrofitOpenSky(): Retrofit

    fun dispatchers(): AppDispatchers
}