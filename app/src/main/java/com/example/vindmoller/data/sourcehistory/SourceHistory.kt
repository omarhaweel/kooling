package com.example.vindmoller.data.sourcehistory

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.vindmoller.data.source.Source
import kotlinx.datetime.Instant

@Entity(
    tableName = "sourceHistory",
    primaryKeys = [
        "sourceId",
        "timestamp"
    ],
    foreignKeys = [
        ForeignKey(
            entity = Source::class,
            parentColumns = ["sourceId"],
            childColumns = ["sourceId"],
        )
    ]
)
data class SourceHistory(
    val sourceId: String,
    val timestamp: Instant,
    val windSpeed: Float,
    val windDirection: Float,
    val windTurbineCount: Int,
)
