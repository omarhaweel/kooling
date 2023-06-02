package com.example.vindmoller.data.settings

import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(private val settingsDao: SettingsDao): SettingsRepository {
    override fun getValue(dataKey: String): Flow<Boolean?> = settingsDao.getValue(dataKey)

    override suspend fun updateValue(dataKey: String, newValue: Boolean) = settingsDao.updateValue(dataKey, newValue)
}