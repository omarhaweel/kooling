package com.example.vindmoller

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vindmoller.network.locationforecast.LocationForecastApi
import com.example.vindmoller.network.locationforecast.LocationForecastApiService
import com.example.vindmoller.network.locationforecast.model.LocationForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class RetrofitLocationForecastDataTest {
    private var locationforecastResponse: LocationForecastResponse? = null
    private val locationForecastApiService: LocationForecastApiService = LocationForecastApi.getService()
    private val lat = 59.9114f
    private val lon = 10.7579f

    /**
     * Suspend  is not allowed in JUnit4,
     * use runBlocking function to create a coroutine scope
     * and the withContext function to switch to an I/O dispatcher to make the API request.
     */
    // Test if we get any response from the Location Forecast API
    @Test
    @Throws(Exception::class)
    fun testAnyResponseFromLocationForecast() = runBlocking {
        withContext(Dispatchers.IO) {
            // Make an API call to get the LocationForecastResponse
            locationforecastResponse = locationForecastApiService.getForecast(lat, lon)
        }
        // Check if the response is not null
        assertNotNull(locationforecastResponse)
    }

    //Test to see if the windspeed gives expected values.
    @Test
    @Throws(Exception::class)
    fun testWindSpeedResponseFromLocationForecast() = runBlocking {
        withContext(Dispatchers.IO) {
            // Make an API call to get the LocationForecastResponse
            locationforecastResponse = locationForecastApiService.getForecast(lat, lon)
        }

        val actualWindSpeed = locationforecastResponse?.properties!!.timeseries[0].data.instant.details.wind_speed

        //Check if the windspeed is 0 or more
        assertTrue(actualWindSpeed>=0)
    }

    //Test that checks we get the expected timestamp in the response
    @Test
    @Throws(Exception::class)
    fun testTimeStampFromLocationForecast() = runBlocking {
        withContext(Dispatchers.IO) {
            // Make an API call to get the LocationForecastResponse
            locationforecastResponse = locationForecastApiService.getForecast(lat, lon)
        }

        val timeStamp = locationforecastResponse?.properties?.timeseries?.get(0)?.time

        // Extract the year from the timestamp and check if it matches the expected year
        val expectedTimeStampYear = "2023"
        val actualTimeStampYear = timeStamp?.slice(0..3)
        assertEquals(expectedTimeStampYear, actualTimeStampYear)

        // Extract the date from the timestamp and check if it matches the current date
        val expectedTimeStamp = LocalDate.now().toString()
        val actualTimeStampDate = timeStamp?.slice(0..9)
        assertEquals(expectedTimeStamp, actualTimeStampDate)

    }
}