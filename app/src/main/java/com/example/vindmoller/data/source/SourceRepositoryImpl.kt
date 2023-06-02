package com.example.vindmoller.data.source


class SourceRepositoryImpl(private val sourceDao: SourceDao): SourceRepository {
   override suspend fun getCountySources(fylkeId: Int): List<Source> = sourceDao.getCountySources(fylkeId)
    override suspend fun getSourceWithSourceHistory(): List<SourceWithSourceHistory> = sourceDao.getSourceWithSourceHistory()

    override suspend fun getSourceWithWindTurbine(): List<SourceWithWindTurbine> = sourceDao.getSourceWithWindTurbine()

    override suspend fun getSourceWithSourceHistory(fylkeId: Int): List<SourceWithSourceHistory> = sourceDao.getSourceWithSourceHistory(fylkeId)

    override suspend fun getSourceWithWindTurbine(fylkeId: Int): List<SourceWithWindTurbine> = sourceDao.getSourceWithWindTurbine(fylkeId)
}