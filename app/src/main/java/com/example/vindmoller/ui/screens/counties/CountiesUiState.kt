package com.example.vindmoller.ui.screens.counties


import com.example.vindmoller.data.fylke.FylkeWithWindTurbines

data class CountiesUiState (
    val countySources: List<FylkeWithWindTurbines> = listOf(), // get a list of all counties with placed wind turbines
)