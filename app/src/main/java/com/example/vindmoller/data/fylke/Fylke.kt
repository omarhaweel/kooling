package com.example.vindmoller.data.fylke

import androidx.room.Entity

@Entity(
    tableName = "fylker",
    primaryKeys = [
        "fylkeId"
    ]
)
data class Fylke(
    val fylkeId: Int,
    val fylkeName: String,
    val color: Int,
)
