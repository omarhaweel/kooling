package com.example.vindmoller.data.composite

import com.example.vindmoller.data.sourcehistory.SourceHistory
import kotlinx.datetime.Instant
// joint interface between the FROST API and room database
interface CompositeRepository {

    suspend fun getLatest(): List<SourceHistory>
    suspend fun updateLatest()
    suspend fun getBetween(
        referenceTimeStart: Instant?,
        referenceTimeEnd: Instant?,
    ): List<SourceHistory>
    suspend fun updateBetween(referenceTimeStart: Instant?, referenceTimeEnd: Instant?)
    suspend fun getMissing(): List<SourceHistory>

    suspend fun updateMissing()

    suspend fun resetWind()

    suspend fun launchDummyState()
}