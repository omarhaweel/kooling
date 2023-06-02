package com.example.vindmoller.data.composite

import com.example.vindmoller.data.fylke.FylkeRepository
import com.example.vindmoller.data.sourcehistory.SourceHistory
import com.example.vindmoller.data.sourcehistory.SourceHistoryRepository
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import com.example.vindmoller.network.frost.FrostApiRepository
import com.example.vindmoller.network.frost.model.toSourceHistory
import com.example.vindmoller.util.Datetime
import kotlinx.datetime.Instant
// joint class between FROST and room database

class CompositeRepositoryImpl(
    private val sourceHistoryRepository: SourceHistoryRepository,
    private val windTurbineRepository: WindTurbineRepository,
    private val frostApiRepository: FrostApiRepository,
    private val fylkeRepository: FylkeRepository
): CompositeRepository {

//        TODO("Not yet implemented")
//    }

    override suspend fun getLatest(): List<SourceHistory> {
        val sourceWindTurbineCountMap = windTurbineRepository.getWindTurbineCountBySource()
        val sourcesWithWindTurbine = sourceWindTurbineCountMap.keys
        val partialSourceHistory = frostApiRepository.getSourceHistoryLatest(
            sourcesWithWindTurbine.toList()
        )
        return partialSourceHistory.map {
            it.toSourceHistory(
                sourceWindTurbineCountMap[it.sourceId]!!
            )
        }
    }
    // updates the latest sourceHostory in sourceHistory tabel
    override suspend fun updateLatest() {
        sourceHistoryRepository.insertSourceHistoryMany(
            getLatest()
        )
    }

    override suspend fun getBetween(
        referenceTimeStart: Instant?,
        referenceTimeEnd: Instant?,
    ): List<SourceHistory> {
        val sourceWindTurbineCountMap = windTurbineRepository.getWindTurbineCountBySource()
        val sourcesWithWindTurbine = sourceWindTurbineCountMap.keys
        val partialSourceHistory = frostApiRepository.getSourceHistoryMany(
            sourcesWithWindTurbine.toList(),
            referenceTimeStart,
            referenceTimeEnd,
        )
        return partialSourceHistory.map {
            it.toSourceHistory(
                sourceWindTurbineCountMap[it.sourceId]!!
            )
        }
    }

    override suspend fun updateBetween(referenceTimeStart: Instant?, referenceTimeEnd: Instant?) {
        sourceHistoryRepository.insertSourceHistoryMany(
            getBetween(referenceTimeStart, referenceTimeEnd)
        )
    }

    override suspend fun getMissing(): List<SourceHistory> {
        val referenceTimeStart = sourceHistoryRepository.getLatestTimestamp()
        val referenceTimeEnd = Datetime.currentTimeRangeEnd()
        return if (referenceTimeStart == null) {
            getLatest()
        } else {
            getBetween(
                referenceTimeStart,
                referenceTimeEnd
            )
        }
    }

    override suspend fun updateMissing() {
        sourceHistoryRepository.insertSourceHistoryMany(
            getMissing()
        )
    }

    override suspend fun resetWind() {
        sourceHistoryRepository.deleteAll()
        windTurbineRepository.deleteAll()
    }

    override suspend fun launchDummyState() {
        resetWind()

        val firstPlacedTime = Instant.fromEpochMilliseconds(System.currentTimeMillis().minus(86400000))

        fylkeRepository.getCountySources().forEach{ fylkeWithSources ->
            windTurbineRepository.insertWindTurbine(WindTurbine(sourceId = fylkeWithSources.sources[0].sourceId, timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis().minus(86400000))))
            windTurbineRepository.insertWindTurbine(WindTurbine(sourceId = fylkeWithSources.sources[1].sourceId, timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis().minus(86400000))))
        }

        val timeNow = Instant.fromEpochMilliseconds(System.currentTimeMillis())

        updateBetween(firstPlacedTime, timeNow)
    }
}