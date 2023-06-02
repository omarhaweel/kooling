package com.example.vindmoller.network.frost.model


import com.example.vindmoller.data.sourcehistory.SourceHistory
import kotlinx.datetime.Instant
// a sourceHistory that lacks windmills
data class PartialSourceHistory(
    val sourceId: String,
    val timestamp: Instant,
    val windSpeed: Float,
)

fun PartialSourceHistory.toSourceHistory(windTurbineCount: Int): SourceHistory {
    return SourceHistory(
        sourceId = this.sourceId,
        timestamp = this.timestamp,
        windSpeed = this.windSpeed,
        windDirection = 0f,
        windTurbineCount = windTurbineCount,
    )
}