package com.example.vindmoller.network.frost

import com.example.vindmoller.network.frost.model.FrostResponse
import com.example.vindmoller.network.frost.model.PartialSourceHistory
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime


// TODO: more resiliant way for grouping wind speed and wind direction
// TODO: better pre-filtering for sources based on also having available wind_from_direction and availabletimeseries
// TODO: maybe, long term, dynamically build this source list

class FrostApiRepositoryImpl(private val frostApiService: FrostApiService): FrostApiRepository {
    private fun toIsoString(instant: Instant): String {
        return instant.toLocalDateTime(TimeZone.UTC).toString()
    }

    private fun floorInstantToNearestTenthMinute(instant: Instant): Instant {
        return instant.minus(
            instant.epochSeconds % 600,
            DateTimeUnit.SECOND,
        )
    }
    private fun parseReferenceTime(rangeStart: Instant?, rangeEnd: Instant?): String? {
        if ((rangeStart == null) || (rangeEnd == null) || (rangeStart > rangeEnd)) {
            return null
        }

        val roundedRangeEnd = floorInstantToNearestTenthMinute(rangeEnd)

        return if (rangeStart == roundedRangeEnd) {
            toIsoString(rangeStart)
        } else {
            "${toIsoString(rangeStart)}/${toIsoString(roundedRangeEnd)}"
        }
    }

    private fun parseSources(sources: List<String>): String? {
        return if (sources.isEmpty()) {
            null
        } else {
            sources.joinToString(",")
        }
    }
    private fun toSourceHistoryOrNull(dataPoint: FrostResponse.FrostDatapoint): PartialSourceHistory? {
        return if (dataPoint.observations.isNotEmpty()) {
            PartialSourceHistory(
                sourceId = dataPoint.sourceId.removeSuffix(":0"), // TODO: make generic for all ints or constrain sensor number in request
                timestamp = Instant.parse(dataPoint.referenceTime),
                windSpeed = dataPoint.observations.first().value,
            )
        } else {
            null
        }
    }
    private fun normalizeFrostResponse(frostResponse: FrostResponse): List<PartialSourceHistory> {
        return frostResponse.data.mapNotNull { toSourceHistoryOrNull(it) }
    }
    private suspend fun callSourceHistory(
        sources: String?,
        referenceTime: String?,
        elements: String?,
    ): List<PartialSourceHistory> {
        return if ((sources == null) || (referenceTime == null) || (elements == null)) {
            listOf()
        } else {
            normalizeFrostResponse(
                frostApiService.getHistory(sources, referenceTime, elements)
            )
        }
    }

    override suspend fun getSourceHistoryMany(sources: List<String>, referenceTimeStart: Instant?, referenceTimeEnd: Instant?): List<PartialSourceHistory> {
        return callSourceHistory(
            sources = parseSources(sources),
            referenceTime =  parseReferenceTime(referenceTimeStart, referenceTimeEnd),
            elements = "wind_speed",
        )
    }

    override suspend fun getSourceHistoryLatest(sources: List<String>): List<PartialSourceHistory> {
        return callSourceHistory(
            sources = parseSources(sources),
            referenceTime = "latest",
            elements = "wind_speed",
        )
    }


}