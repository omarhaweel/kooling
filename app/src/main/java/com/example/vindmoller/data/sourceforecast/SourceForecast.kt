package com.example.vindmoller.data.sourceforecast

import kotlinx.datetime.Instant

data class SourceForecast(
    val sourceId: String,
    val timeUpdated: Instant,
    val timestamp: Instant,
    val windSpeed: Float,
    val windDirection: Float,
)
