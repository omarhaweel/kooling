package com.example.vindmoller.data.sourcehistory

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant


class SourceHistoryRepositoryImpl(private val sourceDao: SourceHistoryDao): SourceHistoryRepository {
    override suspend fun getSourceHistoryBySourceId(sourceId: String): List<SourceHistory> = sourceDao.getSourceHistoryBySourceId(sourceId)

    override suspend fun getAllSourceHistory(): List<SourceHistory> = sourceDao.getAllSourceHistory()
    override suspend fun getLatestTimestamp(): Instant? = sourceDao.getLatestTimestamp()
    override suspend fun insertSourceHistoryMany(sourceHistoryList: List<SourceHistory>) = sourceDao.insertSourceHistoryMany(sourceHistoryList)

    override suspend fun insertSourceHistorySingle(sourceHistory: SourceHistory) = sourceDao.insertSourceHistorySingle(sourceHistory)

    override fun getAllSourceHistoryFlow(): Flow<List<SourceHistory>> = sourceDao.getAllSourceHistoryFlow()

    override fun getSingleSourceHistoryFlow(sourceId: String): Flow<List<SourceHistory>> = sourceDao.getSingleSourceHistoryFlow(sourceId)

    override fun getCountySourceHistoryFlow(fylkeId: Int): Flow<List<SourceHistory>> = sourceDao.getCountySourceHistoryFlow(fylkeId)

    override suspend fun deleteAll() = sourceDao.deleteAll()
}