package com.example.vindmoller.network.locationforecast

import com.example.vindmoller.network.locationforecast.model.LocationForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

// request to location forecast is carried out by providing latitude and logitude
interface LocationForecastApiService {
    @GET("2.0/compact")
    suspend fun getForecast(@Query("lat") lat: Float, @Query("lon") lon: Float): LocationForecastResponse

}