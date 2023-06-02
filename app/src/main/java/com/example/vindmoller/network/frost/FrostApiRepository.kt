package com.example.vindmoller.network.frost

import com.example.vindmoller.network.frost.model.PartialSourceHistory
import kotlinx.datetime.Instant

// repository where viewmodels communicate with to retrieve data related to FROST API, sourceHistory objects
interface FrostApiRepository {

    suspend fun getSourceHistoryMany(sources: List<String>, refereceTimeStart: Instant?, refereceTimeEnd: Instant?): List<PartialSourceHistory>


    suspend fun getSourceHistoryLatest(sources: List<String>): List<PartialSourceHistory>
}