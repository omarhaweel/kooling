package com.example.vindmoller.data.fylke

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
@Dao
interface FylkeDao {
    @Query("SELECT * FROM fylker")
    suspend fun getAllCounties(): List<Fylke>

    @Query("SELECT * FROM fylker WHERE fylkeId = :fylkeId")
    suspend fun getSingleCounty(fylkeId: Int): List<Fylke>

    @Transaction
    @Query("SELECT * FROM fylker")
    suspend fun getCountySources(): List<FylkeWithSources>

    @Transaction
    @Query("SELECT * FROM fylker")
    suspend fun getCountySourceHistory(): List<FylkeWithSourceHistory>

    @Transaction
    @Query("SELECT * FROM fylker")
    suspend fun getCountyWindTurbine(): List<FylkeWithWindTurbines>
}