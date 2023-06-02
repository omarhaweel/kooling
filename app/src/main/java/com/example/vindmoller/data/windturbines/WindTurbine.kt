package com.example.vindmoller.data.windturbines

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.vindmoller.data.source.Source
import kotlinx.datetime.Instant

@Entity(
    tableName = "windturbines",
    foreignKeys = [
        ForeignKey(
            entity = Source::class,
            parentColumns = ["sourceId"],
            childColumns = ["sourceId"],
        )
    ]
)
data class WindTurbine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sourceId: String,
    val timestamp: Instant,
//    val windturbineTypeId: Int,
)
