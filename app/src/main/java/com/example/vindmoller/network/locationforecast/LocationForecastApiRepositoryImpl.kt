package com.example.vindmoller.network.locationforecast


import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.sourceforecast.SourceForecast
import com.example.vindmoller.network.locationforecast.model.LocationForecastResponse
import kotlinx.datetime.Instant

class LocationForecastApiRepositoryImpl(private val locationForecastApiService: LocationForecastApiService): LocationForecastApiRepository {
    private fun normalizeLocationForecaseResponse(sourceId:String, response: LocationForecastResponse): List<SourceForecast> {
        return response.properties.timeseries.map {
            SourceForecast(
                sourceId = sourceId,
                timestamp = Instant.parse(it.time),
                windSpeed = it.data.instant.details.wind_speed,
                windDirection = it.data.instant.details.wind_from_direction,
                timeUpdated = Instant.fromEpochSeconds(0), // TODO
            )
        }
    }
    // get list of sourceForecast objects, right from the endpoint after deserializing data
    override suspend fun getSourceForecast(source: Source): List<SourceForecast> {
        return normalizeLocationForecaseResponse(
            source.sourceId,
            locationForecastApiService.getForecast(source.lat, source.lon)
        )
    }
}

