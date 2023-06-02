package com.example.vindmoller.ui.screens.details


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    windTurbineRepository: WindTurbineRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val sourceId: String = checkNotNull(savedStateHandle["sourceId"])

    val detailsWindTurbineUiState: StateFlow<DetailsWindTurbineUiState> =
        windTurbineRepository.getSourceWindTurbinesFlow(sourceId).map {
            DetailsWindTurbineUiState(
                windTurbines = it
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DetailsWindTurbineUiState()
        )
/*
    val detailsSourceHistoryUiState: StateFlow<DetailsSourceHistoryUiState> =
        sourceHistoryRepository.getSingleSourceHistoryFlow(sourceId).map {
            DetailsSourceHistoryUiState(
                sourceHistory = it,
                sourceHistoryProduction = getTotalProduction(it),

            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DetailsSourceHistoryUiState()
        )
        */

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

