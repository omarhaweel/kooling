package com.example.vindmoller.network.locationforecast


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class LocationForecastApi {
    companion object {
        @Volatile
        private var instance: LocationForecastApiService? = null

        fun getService(): LocationForecastApiService {
            return instance ?: synchronized(this) {
                val baseUrl = "https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/"

                // credentials for authentication to Location Forecast
                val customInterceptor = Interceptor { chain ->
                    val request = chain.request()
                    val newRequest = request.newBuilder()
                        .addHeader("X-Gravitee-API-Key", "010ddeb2-7a49-4507-8897-aa6d7facde14")
                        .build()
                    chain.proceed(newRequest)
                }

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(customInterceptor)
                    .build()

                val json = Json { ignoreUnknownKeys = true }

                val retrofit: Retrofit = Retrofit.Builder()
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .build()

                retrofit.create(LocationForecastApiService::class.java).also { instance = it }
            }
        }
    }
}
