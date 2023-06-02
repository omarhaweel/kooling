package com.example.vindmoller.algorithms


import kotlin.math.pow


// calculate energy produced in Kwh
fun getProducedEnergyOFOneWindSpeed(
    airDensity: Double = 1.25,
    sweptArea: Double = 5000.0,
    powerCoeff: Double = 0.4,
    windSpeed : Double,
    generatorEff : Double = 0.95,
    gearBoxBearingEff : Double = 0.95
): Double {

    // get produced energy in KWh instead of watt by dividing by 1000 ->Kwh
    return (0.5 * airDensity * sweptArea * powerCoeff * windSpeed.pow(3) * generatorEff * gearBoxBearingEff) /1000
}


