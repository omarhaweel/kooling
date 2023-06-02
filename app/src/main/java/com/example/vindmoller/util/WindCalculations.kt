package com.example.vindmoller.util

import kotlin.math.pow

object WindCalculations {
    fun getNOKFromWind(
        kWh: Double
    ): Double {
        return kWh * 1.5
    }

    fun getkWhFromWind(
        windSpeed: Float,
    ): Double {
        return getWattsFromWind(windSpeed) / (1000 * 60 * 60)
    }

    private fun getWattsFromWind(
        windSpeed: Float,
        airDensity: Double = 1.25,
        sweptArea: Double = 5000.0,
        powerCoeff: Double = 0.4,
        generatorEff: Double = 0.95,
        gearBoxBearingEff: Double = 0.95
    ): Double {

        return (0.5 * airDensity * sweptArea * powerCoeff * windSpeed.pow(3) * generatorEff * gearBoxBearingEff)
    }
}