package com.example.vindmoller.data.point

class PointRepositoryImpl(private val pointDao: PointDao): PointRepository {
    override suspend fun getAllPointsWithColor(): List<PointWithColor> = pointDao.getAllPointsWithColor()
}