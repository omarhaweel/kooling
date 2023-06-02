package com.example.vindmoller.data.fylke

import androidx.room.Embedded
import androidx.room.Relation
import com.example.vindmoller.data.source.Source

data class FylkeWithSources(
    @Embedded val fylke: Fylke,
    @Relation(
        parentColumn = "fylkeId",
        entityColumn = "fylkeId",
    )
    val sources: List<Source>
)