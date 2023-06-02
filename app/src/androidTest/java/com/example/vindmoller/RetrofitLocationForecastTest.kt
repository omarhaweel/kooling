package com.example.vindmoller

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

@RunWith(AndroidJUnit4::class)
class RetrofitLocationForecastTest {
    private val locationForecastBaseUrl = "https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/"

    // Define an interceptor to add custom headers to the request
    private val locationForecastCustomInterceptor = Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("X-Gravitee-API-Key", "010ddeb2-7a49-4507-8897-aa6d7facde14")
            .build()
        chain.proceed(newRequest)
    }

    // Create a client with the interceptor
    private val locationForecastOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(locationForecastCustomInterceptor)
        .build()

    // Create a JSON instance for serialization
    private val locationForecastJson = Json { ignoreUnknownKeys = true }

    // Create a Retrofit instance for the Location Forecast API
    private val locationForecastRetrofitInstance: Retrofit = Retrofit.Builder()
        .addConverterFactory(locationForecastJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(locationForecastBaseUrl)
        .client(locationForecastOkHttpClient) // Add the OkHttpClient with the interceptors
        .build()

    // Define the API service interface for Location Forecast
    interface LocationForecastService {
        @GET("2.0/compact?lat=59.9114&lon=10.7579")
//      Use Call<String> to indicate that the response is asynchronous and handled by Retrofit.
        fun getForecast(): Call<String>
    }

    // Create an instance of the LocationForecastService using the Retrofit instance
    private val locationForecastRetrofitService: LocationForecastService by lazy {
        locationForecastRetrofitInstance.create(LocationForecastService::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun getForecastTestReturnCodeIsRange200to300() {
        // Enqueue the request to make an asynchronous call to the API and get the response
        locationForecastRetrofitService.getForecast().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                // Check if the response is successful (status code 2xx)
                assertTrue(response.isSuccessful)
            }
            // Handle failure case if the API call fails
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("My Activity", "response was no succsessful")
            }
        })
    }



}