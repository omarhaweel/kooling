package com.example.vindmoller.data.point

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PointDao {
    @Query("SELECT x,y,fylkeId,color FROM points p JOIN fylker f USING (fylkeId)")
    suspend fun getAllPointsWithColor(): List<PointWithColor>

}