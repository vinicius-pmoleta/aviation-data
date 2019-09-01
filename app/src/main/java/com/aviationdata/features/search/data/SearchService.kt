package com.aviationdata.features.search.data

import com.aviationdata.features.search.data.RawSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("api/metadata/aircraft/list")
    suspend fun search(
        @Query("q") query: String,
        @Query("p") page: Int,
        @Query("n") perPage: Int,
        @Query("sc") sortBy: String,
        @Query("sd") sortOrder: String
    ): RawSearchResponse

}