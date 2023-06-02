package com.example.vindmoller

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vindmoller.network.frost.FrostApi.Companion.getService
import com.example.vindmoller.network.frost.FrostApiService
import com.example.vindmoller.network.frost.model.FrostResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RetrofitFrostTest {
    private var frostResponse: FrostResponse? = null
    private val frostApiService: FrostApiService = getService()
    private val source = "SN26990:0" //Vestfold og Telemark
    private val now = Instant.fromEpochMilliseconds(System.currentTimeMillis())
    private val then = now.minus(DateTimePeriod(hours = 1), TimeZone.UTC)
    private val referenceTime = "${toIsoString(then.epochSeconds)}/${toIsoString(now.epochSeconds)}"
    private val elements = "wind_speed"

    // Test that we get a good response back from the Frost API
    @Test
    @Throws(Exception::class)
    fun testAnyResponseFromAPI() = runBlocking {
        withContext(Dispatchers.IO) {
            // Make an API call to get the FrostResponse
            frostResponse = frostApiService.getHistory(source, referenceTime, elements)
        }
        // Check if the response is not null
        assertNotNull(frostResponse)
    }

    // Convert epoch seconds to ISO string format
    private fun toIsoString(epochSeconds: Long): String {
        return Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(TimeZone.UTC).toString()
    }


    // Test that the source we send is the same source we get back in the response
    @Test
    @Throws(Exception::class)
    fun testCorrectSourceID() = runBlocking  {
        // Make an API call to get the FrostResponse
        withContext(Dispatchers.IO) {
            frostResponse = frostApiService.getHistory(source, referenceTime, elements)
        }
        // Get the source ID from the FrostResponse
        val getData = frostResponse?.data?.get(0)?.sourceId

        val expectedSource = source
        val actualSource = getData.toString()

        // Check if the retrieved source ID matches the expected source ID
        assertEquals(expectedSource, actualSource)
    }

    @Test
    @Throws(Exception::class)
    fun testCorrectNumberOfObservations() = runBlocking  {
        withContext(Dispatchers.IO) {
            frostResponse = frostApiService.getHistory(source, referenceTime, elements)
        }

        // Make an API call to get the FrostResponse
        val actualValue = frostResponse?.data?.get(0)?.observations?.size
        val expectedValue = 2

        // Check if the retrieved number of observations matches the expected value
        assertEquals(expectedValue, actualValue)
    }
}