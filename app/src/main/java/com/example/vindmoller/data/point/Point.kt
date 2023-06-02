package com.example.vindmoller.data.point

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.vindmoller.data.fylke.Fylke

@Entity(
    tableName = "points",
    primaryKeys = ["x","y"],
    foreignKeys = [
        ForeignKey(
            entity = Fylke::class,
            parentColumns = ["fylkeId"],
            childColumns = ["fylkeId"]
        )
    ]
)
data class Point(
    val x: Int,
    val y: Int,
    val fylkeId: Int
)
