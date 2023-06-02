package com.example.vindmoller.ui.screens.map


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vindmoller.data.fylke.Fylke
import com.example.vindmoller.data.fylke.FylkeRepository
import com.example.vindmoller.data.point.PointRepository
import com.example.vindmoller.data.point.PointWithColor
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.source.SourceRepository
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import com.example.vindmoller.network.locationforecast.LocationForecastApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import retrofit2.HttpException
import java.io.IOException

class MapViewModel(
    private val pointRepository: PointRepository,
    private val locationForecastApiRepository: LocationForecastApiRepository,
    private val sourceRepository: SourceRepository,
    private val windTurbineRepository: WindTurbineRepository,
    private val fylkeRepository: FylkeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val mapUiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    mapPoints = pointRepository.getAllPointsWithColor(),
                    availableCounties = fylkeRepository.getAllCounties(),
                )
            }
        }
    }

    // sets the selected county when choosing from drop down or clicking on relevant county on map
    fun setSelectedCounty(fylke: Fylke) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedCounty = fylke,
                    selectedSource = null,
                    // replace with getAllSources to be able to choose
                    availableSources = sourceRepository.getCountySources(fylke.fylkeId),
                    selectedSourceForecast = LocationForecastData.Loading,
                    selectedSourceWindTurbines = listOf(),
                )
            }
        }
    }
        // sets thge selected source, from meny or when clicking, called from Map screen
    fun setSelectedSource(source: Source) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedSource = source,
                    selectedSourceWindTurbines = windTurbineRepository.getSourceWindTurbines(source.sourceId),
                    selectedSourceForecast = try {
                        LocationForecastData.Success(
                            locationForecastApiRepository.getSourceForecast(
                                source
                            )
                        )
                    } catch (e: IOException) {
                        LocationForecastData.Error
                    } catch (e: HttpException) {
                        LocationForecastData.Error
                    }
                )
            }
        }
    }
    // ad a wind turbine , called from the bottom sheet
    fun addWindTurbine(source: Source) {
        viewModelScope.launch {
            windTurbineRepository.insertWindTurbine(
                WindTurbine(
                    sourceId = source.sourceId,
                    timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis())
                )
            )
            _uiState.update {
                it.copy(
                    selectedSourceWindTurbines = windTurbineRepository.getSourceWindTurbines(source.sourceId)
                )
            }
        }
    }
    // updates selected couty from choosing a point on the grid map
    fun setCountyFromPoint(point: PointWithColor) {
        viewModelScope.launch {
            val fylke = fylkeRepository.getSingleCounty(point.fylkeId).first()
            if (fylke != null) {
                val availableSources = sourceRepository.getCountySources(fylke.fylkeId)
                _uiState.update {
                    it.copy(
                        selectedCounty = fylke,
                        availableSources = availableSources,
                        selectedSource = null,
                        selectedSourceForecast = LocationForecastData.Loading
                    )
                }
            }
        }
    }




    // delete the wind turbine
    fun deleteWindTurbine(windTurbine: WindTurbine, source: Source) {
        viewModelScope.launch {
            windTurbineRepository.deleteWindTurbine(
                windTurbine.id
            )
            _uiState.update {
                it.copy(
                    selectedSourceWindTurbines = windTurbineRepository.getSourceWindTurbines(source.sourceId)
                )
            }
        }
    }

}


