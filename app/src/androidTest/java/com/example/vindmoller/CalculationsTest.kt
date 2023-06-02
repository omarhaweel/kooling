package com.example.vindmoller

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vindmoller.algorithms.getProducedEnergyOFOneWindSpeed
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculationsTest {
//
    @Test
    @Throws(Exception::class)
    fun testGetProducedEnergyOFOneWindSpeed() {
        // Define the wind speeds to test
        val windSpeed1 = 5.0
        val windSpeed2 = 10.0
        // Define the wind speeds to test
        val expectedValue5 = 141.015625
        val expectedValue10 = 1128.125

        // Call the function getProducedEnergyOFOneWindSpeed with windSpeed1&2 and store the result
        val actualValue5 = getProducedEnergyOFOneWindSpeed(windSpeed = windSpeed1)
        val actualValue10 = getProducedEnergyOFOneWindSpeed(windSpeed = windSpeed2)

        // Assert that the actual produced energy values match the expected values for each wind speed
        assertEquals(expectedValue5, actualValue5)
        assertEquals(expectedValue10, actualValue10)

    }


}