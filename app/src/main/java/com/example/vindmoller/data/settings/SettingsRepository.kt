package com.example.vindmoller.data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getValue(dataKey: String): Flow<Boolean?>

    suspend fun updateValue(dataKey: String, newValue: Boolean)
}