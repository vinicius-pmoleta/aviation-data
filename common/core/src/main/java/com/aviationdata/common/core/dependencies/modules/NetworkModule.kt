package com.aviationdata.common.core.dependencies.modules

import com.aviationdata.common.core.dependencies.KodeinTags
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = Kodein.Module("network") {

    bind() from singleton {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    bind(KodeinTags.REMOTE_SOURCE_OPEN_SKY) from singleton {
        RetrofitBuilder.build(
            url = "https://opensky-network.org/",
            client = instance()
        )
    }

    bind(KodeinTags.REMOTE_SOURCE_JET_PHOTOS) from singleton {
        RetrofitBuilder.build(
            url = "https://www.jetphotos.com/",
            client = instance()
        )
    }
}

object RetrofitBuilder {

    fun build(url: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}