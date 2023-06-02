package com.example.vindmoller.network.locationforecast.model

import kotlinx.serialization.Serializable

// TODO: get altitude from geometry field
@Serializable
data class LocationForecastResponse(val properties: Properties) {

    @Serializable
    data class Properties(val timeseries: List<Datapoint>)

    @Serializable
    data class Datapoint(val time: String, val data: Data)

    @Serializable
    data class Data(val instant: Instant)

    @Serializable
    data class Instant(val details: Details)

    @Serializable
    data class Details(val wind_from_direction: Float, val wind_speed: Float)
}