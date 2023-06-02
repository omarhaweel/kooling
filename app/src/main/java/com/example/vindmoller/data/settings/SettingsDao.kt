package com.example.vindmoller.data.settings

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT dataValue FROM settings WHERE dataKey = :dataKey")
    fun getValue(dataKey: String): Flow<Boolean?>

    @Query("UPDATE settings SET dataValue = :newValue WHERE dataKey = :dataKey")
    suspend fun updateValue(dataKey: String, newValue: Boolean)
}