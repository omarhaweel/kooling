package com.example.vindmoller.data.source

interface SourceRepository {
    suspend fun getCountySources(fylkeId: Int): List<Source>

    suspend fun getSourceWithSourceHistory(): List<SourceWithSourceHistory>

    suspend fun getSourceWithWindTurbine(): List<SourceWithWindTurbine>

    suspend fun getSourceWithSourceHistory(fylkeId: Int): List<SourceWithSourceHistory>

    suspend fun getSourceWithWindTurbine(fylkeId: Int): List<SourceWithWindTurbine>

//    suspend fun getManySourceForSingleCounty(fylkeId: Int): List<Source> {
//
//    }
}