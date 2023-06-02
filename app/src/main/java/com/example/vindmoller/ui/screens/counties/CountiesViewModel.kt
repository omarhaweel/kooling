package com.example.vindmoller.ui.screens.counties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vindmoller.data.fylke.FylkeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountiesViewModel (
    private val fylkeRepository: FylkeRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(CountiesUiState())
    val countiesUiState: StateFlow<CountiesUiState> = _uiState.asStateFlow()

// update countySources by callinf fylkeRepository
    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    countySources = fylkeRepository.getCountyWindTurbine(),
                )
            }
        }
    }
}