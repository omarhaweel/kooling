package com.example.vindmoller.ui.screens.map

import com.example.vindmoller.data.fylke.Fylke
import com.example.vindmoller.data.point.PointWithColor
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.sourceforecast.SourceForecast
import com.example.vindmoller.data.windturbines.WindTurbine

sealed interface LocationForecastData {
    data class Success(val sourceForecasts: List<SourceForecast>) : LocationForecastData
    object Error : LocationForecastData
    object Loading : LocationForecastData
}

//Ui State data class for Map Screen

data class MapUiState(
    val availableCounties: List<Fylke> = listOf(),
    val availableSources: List<Source> = listOf(),
    val selectedCounty: Fylke? = null,
    val selectedSource: Source? =  null,
    val mapPoints: List<PointWithColor> = listOf(),
    val selectedSourceForecast: LocationForecastData = LocationForecastData.Loading,
    val selectedSourceWindTurbines: List<WindTurbine> = listOf(),
)

