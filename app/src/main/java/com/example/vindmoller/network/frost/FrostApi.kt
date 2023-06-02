package com.example.vindmoller.network.frost


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

// frost API Service
abstract class FrostApi {

    companion object {
        @Volatile
        private var instance: FrostApiService? = null

        fun getService(): FrostApiService {
            return instance ?: synchronized(this) {
                val baseUrl = "https://frost.met.no/"

                // authentication to FROST, credentials appear as plain text
                val frostBasicAuthInterceptor = Interceptor { chain ->
                    val credentials = Credentials.basic("582c8c4d-dcf4-4593-8f0c-144ca5b1edd4", "e8eabe12-a5ca-46c3-a935-c53617c514b7")
                    val request = chain.request()
                    val newRequest = request.newBuilder().header("Authorization", credentials).build()
                    chain.proceed(newRequest)
                }

                val frostClient = OkHttpClient.Builder()
                    .addInterceptor(frostBasicAuthInterceptor)
                    .build()

                val json = Json { ignoreUnknownKeys = true }

                val retrofit: Retrofit = Retrofit.Builder()
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .baseUrl(baseUrl)
                    .client(frostClient)
                    .build()

                retrofit.create(FrostApiService::class.java).also { instance = it }
            }
        }
    }
}
