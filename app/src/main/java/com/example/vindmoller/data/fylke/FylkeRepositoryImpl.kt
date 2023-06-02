package com.example.vindmoller.data.fylke

class FylkeRepositoryImpl(private val fylkeDao: FylkeDao): FylkeRepository {
    override suspend fun getAllCounties(): List<Fylke> = fylkeDao.getAllCounties()

    override suspend fun getSingleCounty(fylkeId: Int): List<Fylke> = fylkeDao.getSingleCounty(fylkeId)
    override suspend fun getCountySources(): List<FylkeWithSources> = fylkeDao.getCountySources()

    override suspend fun getCountySourceHistory(): List<FylkeWithSourceHistory> = fylkeDao.getCountySourceHistory()

    override suspend fun getCountyWindTurbine(): List<FylkeWithWindTurbines> = fylkeDao.getCountyWindTurbine()
}