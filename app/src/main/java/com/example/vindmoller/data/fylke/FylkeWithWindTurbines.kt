package com.example.vindmoller.data.fylke

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.windturbines.WindTurbine

data class FylkeWithWindTurbines(
    @Embedded val fylke: Fylke,
    @Relation(
        parentColumn = "fylkeId",
        entityColumn = "sourceId",
        associateBy = Junction(
            value = Source::class,
            parentColumn = "fylkeId",
            entityColumn = "sourceId"
        )
    )
    val windTurbines: List<WindTurbine>
)