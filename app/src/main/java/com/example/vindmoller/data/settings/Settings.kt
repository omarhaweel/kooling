package com.example.vindmoller.data.settings

import androidx.room.Entity

@Entity(
    tableName = "settings",
    primaryKeys = [
        "dataKey"
    ]
)
data class Settings(
    val dataKey: String,
    val dataValue: Boolean,
)
