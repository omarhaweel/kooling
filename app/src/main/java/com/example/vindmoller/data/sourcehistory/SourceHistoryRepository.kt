package com.example.vindmoller.data.sourcehistory

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface SourceHistoryRepository {
    suspend fun getSourceHistoryBySourceId(sourceId: String): List<SourceHistory>
    suspend fun getAllSourceHistory(): List<SourceHistory>

    suspend fun getLatestTimestamp(): Instant?

    suspend fun insertSourceHistoryMany(sourceHistoryList: List<SourceHistory>)

    suspend fun insertSourceHistorySingle(sourceHistory: SourceHistory)

    fun getAllSourceHistoryFlow(): Flow<List<SourceHistory>>

    fun getSingleSourceHistoryFlow(sourceId: String): Flow<List<SourceHistory>>

    fun getCountySourceHistoryFlow(fylkeId: Int): Flow<List<SourceHistory>>

    suspend fun deleteAll()
}