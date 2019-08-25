package com.aviationdata.features.search

import com.aviationdata.BuildConfig
import com.aviationdata.core.Aircraft
import com.aviationdata.features.search.data.toAircraft
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DEFAULT_SEARCH_RESULTS_PER_PAGE = 50

class SearchRemoteRepository {

    private val service: SearchService

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://opensky-network.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(SearchService::class.java)
    }

    suspend fun search(query: String, page: Int): List<Aircraft> {
        return service
            .search(query, page, DEFAULT_SEARCH_RESULTS_PER_PAGE, "", "")
            .results
            .map { it.toAircraft() }
    }
}