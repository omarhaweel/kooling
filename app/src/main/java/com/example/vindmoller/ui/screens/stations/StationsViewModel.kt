package com.example.vindmoller.ui.screens.stations


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vindmoller.data.fylke.FylkeRepository
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.source.SourceRepository
import com.example.vindmoller.data.sourcehistory.SourceHistory
import com.example.vindmoller.data.sourcehistory.SourceHistoryRepository
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import com.example.vindmoller.util.WindCalculations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StationsViewModel(
    private val fylkeRepository: FylkeRepository,
    private val windTurbineRepository: WindTurbineRepository,
    private val sourceRepository: SourceRepository,
    private val sourceHistoryRepository: SourceHistoryRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val fylkeId: Int = checkNotNull(savedStateHandle["fylkeId"])
    private val _uiState = MutableStateFlow(StationsUiState())
    val stationsUiState: StateFlow<StationsUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fylke = fylkeRepository.getSingleCounty(fylkeId).firstOrNull()
                )
            }
        }
    }
    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    sources = sourceRepository.getCountySources(fylkeId),  // get all the sources in this fylke
                )
            }
        }
    }
    // get windTurbines in the source chosen one
    fun getSourceWindTurbines(sourceId:String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    windTurbines =  windTurbineRepository.getSourceWindTurbines(sourceId)
                )
            }
        }
    }
    // fjern turbines
    fun removeALLTurbinesInCounty(source: Source) {
        viewModelScope.launch {
            windTurbineRepository.removeSourceTurbines(source.sourceId)
            _uiState.update {
                it.copy(
                    selectedSourceWindTurbines = windTurbineRepository.getSourceWindTurbines(source.sourceId)
                )
            }
        }
    }

    fun getSourceHistoryData(sourceId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    sourceHistoryData = sourceHistoryRepository.getSourceHistoryBySourceId(sourceId)
                )
            }
        }
    }

    fun getTotalProduction(sourceHistory: List<SourceHistory>): Double {
        return sourceHistory.sumOf {
            WindCalculations.getkWhFromWind(it.windSpeed) * 10 * it.windTurbineCount
        }
    }

    fun getAvoidedEmission(totalProduction: Double, kgCo2PerKwh: Double): Double {
        return totalProduction * kgCo2PerKwh
    }

    fun getSavedOilBarrelsCounty(totalProduction: Double): Double {
        return totalProduction / 1_700
    }

}