package com.example.vindmoller.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vindmoller.data.settings.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    //
    init {
        viewModelScope.launch {
            settingsRepository.getValue("darkMode").collect { value ->
                _uiState.update {
                    it.copy(
                        darkMode = value ?: false, // if Null, it is a light mode, else value
                    )
                }
            }
        }

        viewModelScope.launch {
            settingsRepository.getValue("overrideSystemTheme").collect { value ->
                _uiState.update {
                    it.copy(
                        overrideSystemTheme = value ?: false, // if Null, no override, else value (comes from the switch in the settings screen )
                    )
                }
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
           settingsRepository.updateValue("darkMode", !settingsUiState.value.darkMode)
        }
    }

    fun toggleOverride() {
        viewModelScope.launch {
            settingsRepository.updateValue("overrideSystemTheme", !settingsUiState.value.overrideSystemTheme)
        }
    }
}