package com.example.vindmoller.network.locationforecast

import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.sourceforecast.SourceForecast

interface LocationForecastApiRepository {
    suspend fun getSourceForecast(source: Source): List<SourceForecast>
}
// testing connection with database