package com.example.vindmoller.data.windturbines

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant


interface WindTurbineRepository {
    suspend fun getAllWindTurbines(): List<WindTurbine>
    suspend fun getSourceWindTurbines(sourceId: String): List<WindTurbine>
    suspend fun getAllWindTurbineStream(): List<WindTurbine>
    suspend fun insertWindTurbine(windTurbine: WindTurbine)
    suspend fun deleteWindTurbine(id: Int)

    suspend fun getWindTurbinesPlacedAfter(timestamp: Instant): List<WindTurbine>
    suspend fun getNewestWindTurbine(): WindTurbine?

    // ADDED
    suspend fun removeSourceTurbines(sourceId: String)

    //ADDED
    suspend fun getCountyWindTurbines(fylkeId: Int): List<WindTurbine>

   fun getAllWindTurbinesFlow(): Flow<List<WindTurbine>>

    fun getSourceWindTurbinesFlow(sourceId: String): Flow<List<WindTurbine>>

    fun getCountyWindTurbinesFlow(fylkeId: Int): Flow<List<WindTurbine>>

    suspend fun getWindTurbineCountBySource(): Map<String, Int>

    suspend fun getWindTurbineCountBySourceInCounty(fylkeId: Int): Map<String, Int>

    suspend fun getWindTurbineCountByCounty(): Map<Int, Int>

    suspend fun deleteAll()
}