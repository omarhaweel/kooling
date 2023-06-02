package com.example.vindmoller.data.windturbines

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WindTurbineDao {
    @Query("SELECT * FROM windturbines")
    suspend fun getAllWindTurbines(): List<WindTurbine>

    @Query("SELECT * FROM windturbines")
    fun getAllWindTurbinesFlow(): Flow<List<WindTurbine>>

    @Query("SELECT * FROM windturbines WHERE sourceId = :sourceId")
    fun getSourceWindTurbinesFlow(sourceId: String): Flow<List<WindTurbine>>

    @Query("SELECT * FROM windturbines WHERE timestamp > :timestamp")
    suspend fun getWindTurbinesPlacedAfter(timestamp: Long): List<WindTurbine>

    @Query("SELECT * FROM windturbines w JOIN sources s USING (sourceId) WHERE s.fylkeId = :fylkeId")
    fun getCountyWindTurbinesFlow(fylkeId: Int): Flow<List<WindTurbine>>

    @Query("SELECT * FROM windturbines WHERE sourceId = :sourceId")
    suspend fun getSourceWindTurbines(sourceId: String): List<WindTurbine>

    @Query("SELECT w.id, w.sourceId, w.timestamp FROM windturbines w JOIN sources s USING (sourceId) WHERE fylkeId = :fylkeId;")
    suspend fun getCountyWindTurbines(fylkeId: Int): List<WindTurbine>

//    @Query("SELECT COUNT(*) FROM windturbines")
//    suspend fun countAllWindTurbines(): Int
//    @Query("SELECT COUNT(*) FROM windturbines WHERE sourceId = :sourceId")
//    suspend fun countSourceWindTurbines(sourceId: String): Int
//
//    @Query( "SELECT COUNT(*) " +
//            "FROM windturbines w" +
//            "JOIN sources s USING (sourceId)" +
//            "WHERE fylkeId = :fylkeId")
//    suspend fun countCountyWindTurbines(fylkeId: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWindTurbine(windTurbine: WindTurbine)

    @Query("DELETE FROM windturbines WHERE id = :id")
    suspend fun deleteWindTurbine(id: Int)

    @Query("SELECT * FROM windturbines ORDER BY 'id' DESC LIMIT 1")
    suspend fun getNewestWindTurbine(): List<WindTurbine>

    // FJER
    @Query("DELETE FROM windturbines WHERE sourceId = :sourceId")
    suspend fun removeSourceTurbines(sourceId: String)

    @Query("""
        SELECT s.fylkeId, COUNT(*) AS windTurbineCount 
        FROM windturbines w 
        JOIN sources s
            USING (sourceId) 
        GROUP BY fylkeId
    """)
    suspend fun getWindTurbineCountByCounty(): List<CountyWindTurbineCount>

    @Query("""
        SELECT s.sourceId, COUNT(*) AS windTurbineCount 
        FROM windturbines w 
        JOIN sources s
            USING (sourceId) 
        WHERE s.fylkeId = :fylkeId
        GROUP BY sourceId
        """)
    suspend fun getWindTurbineCountBySourceInCounty(fylkeId: Int): List<SourceWindTurbineCount>

    @Query("""
        SELECT sourceId, COUNT(*) AS windTurbineCount
        FROM windturbines
        GROUP BY sourceId
        """)
    suspend fun getWindTurbineCountBySource(): List<SourceWindTurbineCount>

    @Query("DELETE FROM windturbines")
    suspend fun deleteAll()

    data class SourceWindTurbineCount(
        val sourceId: String,
        val windTurbineCount: Int = 0
    )

    data class CountyWindTurbineCount(
        val fylkeId: Int,
        val windTurbineCount: Int = 0
    )

}