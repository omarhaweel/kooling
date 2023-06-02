package com.example.vindmoller.data.source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SourceDao {
    @Query("SELECT * FROM sources WHERE fylkeId = :fylkeId")
    suspend fun getCountySources(fylkeId: Int): List<Source>

    @Transaction
    @Query("SELECT * FROM sources")
    suspend fun getSourceWithSourceHistory(): List<SourceWithSourceHistory>

    @Transaction
    @Query("SELECT * FROM sources WHERE fylkeId = :fylkeId")
    suspend fun getSourceWithSourceHistory(fylkeId: Int): List<SourceWithSourceHistory>
    @Transaction
    @Query("SELECT * FROM sources")
    suspend fun getSourceWithWindTurbine(): List<SourceWithWindTurbine>

    @Transaction
    @Query("SELECT * FROM sources WHERE fylkeId = :fylkeId")
    suspend fun getSourceWithWindTurbine(fylkeId: Int): List<SourceWithWindTurbine>
}