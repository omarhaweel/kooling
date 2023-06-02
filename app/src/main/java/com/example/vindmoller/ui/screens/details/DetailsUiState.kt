package com.example.vindmoller.ui.screens.details

import com.example.vindmoller.data.windturbines.WindTurbine


// source windturbines to be updated in viewModel
data class DetailsWindTurbineUiState(
    val windTurbines: List<WindTurbine> = listOf()

)