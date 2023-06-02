package com.example.vindmoller.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vindmoller.data.sourcehistory.SourceHistory
import com.example.vindmoller.data.sourcehistory.SourceHistoryRepository
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import com.example.vindmoller.util.WindCalculations
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class HomeViewModel (
    private val windTurbineRepository: WindTurbineRepository,
    private val sourceHistoryRepository: SourceHistoryRepository
): ViewModel() {
        private val _uiState = MutableStateFlow(CurrentProductionContainer())

    init {
            viewModelScope.launch {
                val latest = sourceHistoryRepository.getLatestTimestamp() ?: Instant.fromEpochSeconds(0)
                _uiState.update {
                    it.copy(
                        extraTurbines = windTurbineRepository.getWindTurbinesPlacedAfter(latest)
                    )
                }
            }
        }

    // shows total energy production
        private fun getTotalProduction(sourceHistory: List<SourceHistory>): Double {
            return sourceHistory.sumOf {
                WindCalculations.getkWhFromWind(it.windSpeed) * 10 * it.windTurbineCount
            }
        }


        val windTurbines: StateFlow<WindTurbineContainer> =
            getWindTurbine().map {
                WindTurbineContainer(
                    windTurbines = it
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WindTurbineContainer()
            )

        val sourceHistory: StateFlow<SourceHistoryContainer> =
            getSourceHistory().map {
                SourceHistoryContainer(
                    sourceHistory = it,
                    sourceHistoryProduction = getTotalProduction(it)
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SourceHistoryContainer()
            )

        companion object {
            private const val TIMEOUT_MILLIS = 5_000L
        }


    private fun getSourceHistory(): Flow<List<SourceHistory>> {
        return sourceHistoryRepository.getAllSourceHistoryFlow()
    }

    private fun getWindTurbine(): Flow<List<WindTurbine>> {
        return windTurbineRepository.getAllWindTurbinesFlow()
    }

    fun getSavedOilBarrels(totalProduction: Double): Double {
        return totalProduction / 1_700
    }
}


data class WindTurbineContainer(
    val windTurbines: List<WindTurbine> = listOf()
)

data class SourceHistoryContainer(
    val sourceHistory: List<SourceHistory> = listOf(),
    val sourceHistoryProduction: Double = 0.0,
)

data class CurrentProductionContainer(
    val extraTurbines: List<WindTurbine> = listOf(),
)