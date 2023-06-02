package com.example.vindmoller.ui.screens.stations


import com.example.vindmoller.data.fylke.Fylke
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.sourcehistory.SourceHistory
import com.example.vindmoller.data.windturbines.WindTurbine

data class StationsUiState(
    val fylke: Fylke? = null,
    val windTurbines: List<WindTurbine> = listOf(),
    val sources: List<Source> = listOf(),
    val selectedSource: Source? =  null,
    val selectedSourceWindTurbines: List<WindTurbine> = listOf(),
    val sourceHistoryData: List<SourceHistory> = listOf(),
)