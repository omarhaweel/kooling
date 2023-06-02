package com.example.vindmoller.data.source

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.vindmoller.data.fylke.Fylke

@Entity(
    tableName = "sources",
    foreignKeys = [
        ForeignKey(
            entity = Fylke::class,
            parentColumns = ["fylkeId"],
            childColumns = ["fylkeId"],
        )
    ],
    primaryKeys = [
        "sourceId"
    ]
)
data class Source(
    val sourceId: String,
    val lat: Float,
    val lon: Float,
    val fylkeId: Int,
    val stedsnavn: String,
    val topografi: String,
)
