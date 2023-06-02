package com.example.vindmoller.data.point

interface PointRepository {
    suspend fun getAllPointsWithColor(): List<PointWithColor>
}