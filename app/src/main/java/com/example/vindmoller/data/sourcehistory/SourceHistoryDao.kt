package com.example.vindmoller.data.sourcehistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface SourceHistoryDao {
    @Query("SELECT * FROM sourceHistory WHERE sourceId = :sourceId")
    suspend fun getSourceHistoryBySourceId(sourceId: String): List<SourceHistory>
    @Query("SELECT * FROM sourceHistory")
    suspend fun getAllSourceHistory(): List<SourceHistory>

    @Query("SELECT * FROM sourceHistory")
    fun getAllSourceHistoryFlow(): Flow<List<SourceHistory>>

    @Query("SELECT * FROM sourceHistory WHERE sourceId = :sourceId")
    fun getSingleSourceHistoryFlow(sourceId: String): Flow<List<SourceHistory>>

    @Query("SELECT sh.* FROM sourceHistory sh JOIN sources s USING (sourceId) WHERE s.fylkeId = :fylkeId")
    fun getCountySourceHistoryFlow(fylkeId: Int): Flow<List<SourceHistory>>

    @Query("SELECT MAX(timestamp) FROM sourceHistory")
    suspend fun getLatestTimestamp(): Instant?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSourceHistoryMany(sourceHistoryList: List<SourceHistory>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSourceHistorySingle(sourceHistory: SourceHistory)

    @Query("DELETE FROM sourceHistory")
    suspend fun deleteAll()

}