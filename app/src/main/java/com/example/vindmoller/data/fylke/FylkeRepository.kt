package com.example.vindmoller.data.fylke


interface FylkeRepository {
    // funksjoner for å hente data fra databasen, signatur og parametre tyder på funksjonalitet
    suspend fun getAllCounties(): List<Fylke>

    suspend fun getSingleCounty(fylkeId: Int): List<Fylke>

    suspend fun getCountySources(): List<FylkeWithSources>

    suspend fun getCountySourceHistory(): List<FylkeWithSourceHistory>

    suspend fun getCountyWindTurbine(): List<FylkeWithWindTurbines>
}