package com.example.vindmoller.network.frost

import com.example.vindmoller.network.frost.model.FrostResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FrostApiService {
    //TODO: reference time converter function
    //TODO: add params like elements=wind_speed as defaults in param of @GET(..

    @GET("observations/v0.jsonld")
    suspend fun getHistory(
        @Query("sources") sources: String,
        @Query("referencetime") referenceTime: String,
        @Query("elements") elements: String,
    ): FrostResponse
}