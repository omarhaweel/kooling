package com.example.vindmoller.data.source

import androidx.room.Embedded
import androidx.room.Relation
import com.example.vindmoller.data.windturbines.WindTurbine

data class SourceWithWindTurbine(
    @Embedded val source: Source,
    @Relation(
        parentColumn = "sourceId",
        entityColumn = "sourceId",
    )
    val windTurbines: List<WindTurbine>
)