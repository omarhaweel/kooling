package com.example.vindmoller.data.windturbines

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

class WindTurbineRepositoryImpl(private val windTurbineDao: WindTurbineDao): WindTurbineRepository {
    override suspend fun getAllWindTurbines(): List<WindTurbine> = windTurbineDao.getAllWindTurbines()
    override suspend fun getSourceWindTurbines(sourceId: String): List<WindTurbine> = windTurbineDao.getSourceWindTurbines(
        sourceId
    )

    override suspend fun getWindTurbinesPlacedAfter(timestamp: Instant): List<WindTurbine> = windTurbineDao.getWindTurbinesPlacedAfter(timestamp.epochSeconds)

    override suspend fun getAllWindTurbineStream(): List<WindTurbine> = windTurbineDao.getAllWindTurbines()

    override suspend fun insertWindTurbine(windTurbine: WindTurbine) = windTurbineDao.insertWindTurbine(windTurbine)
    override suspend fun deleteWindTurbine(id: Int) = windTurbineDao.deleteWindTurbine(id)

    override suspend fun getNewestWindTurbine(): WindTurbine? = windTurbineDao.getNewestWindTurbine().firstOrNull()
    // ADDED OMAR
    override suspend fun removeSourceTurbines(sourceId: String) = windTurbineDao.removeSourceTurbines(sourceId)
    // ADDED OMAR
    override suspend fun getCountyWindTurbines(fylkeId: Int) = windTurbineDao.getCountyWindTurbines(fylkeId)

    override fun getAllWindTurbinesFlow(): Flow<List<WindTurbine>> = windTurbineDao.getAllWindTurbinesFlow()
    override fun getSourceWindTurbinesFlow(sourceId: String): Flow<List<WindTurbine>> = windTurbineDao.getSourceWindTurbinesFlow(sourceId)

    override fun getCountyWindTurbinesFlow(fylkeId: Int): Flow<List<WindTurbine>> = windTurbineDao.getCountyWindTurbinesFlow(fylkeId)

    override suspend fun getWindTurbineCountByCounty(): Map<Int, Int> = windTurbineDao.getWindTurbineCountByCounty().associate { it.fylkeId to it.windTurbineCount }
    override suspend fun getWindTurbineCountBySource(): Map<String, Int> = windTurbineDao.getWindTurbineCountBySource().associate { it.sourceId to it.windTurbineCount }
    override suspend fun getWindTurbineCountBySourceInCounty(fylkeId: Int): Map<String, Int> = windTurbineDao.getWindTurbineCountBySourceInCounty(fylkeId).associate { it.sourceId to it.windTurbineCount }

    override suspend fun deleteAll() = windTurbineDao.deleteAll()
}