package com.aviationdata.core.dependencies

import com.aviationdata.BuildConfig
import com.aviationdata.core.structure.AppDispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val applicationModule = Kodein.Module("application") {

    bind() from provider {
        AppDispatchers()
    }
}

val networkModule = Kodein.Module("network") {

    bind() from provider {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    bind() from provider {
        Retrofit.Builder()
            .baseUrl("https://opensky-network.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(instance())
            .build()
    }
}