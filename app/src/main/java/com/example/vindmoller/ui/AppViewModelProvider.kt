/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vindmoller.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vindmoller.WindInitializer
import com.example.vindmoller.ui.screens.details.DetailsViewModel
import com.example.vindmoller.ui.screens.stations.StationsViewModel
//import com.example.vindmoller.ui.screens.gridmap.GridMapViewModel
import com.example.vindmoller.ui.screens.map.MapViewModel
import com.example.vindmoller.ui.screens.home.HomeViewModel
import com.example.vindmoller.ui.screens.settings.SettingsViewModel
import com.example.vindmoller.ui.screens.counties.CountiesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                windApplication().container.windTurbineRepository,
                windApplication().container.sourceHistoryRepository
            )
        }

        initializer {
            MapViewModel(
                windApplication().container.pointRepository,
                windApplication().container.locationForecastApiRepository,
                windApplication().container.sourceRepository,
                windApplication().container.windTurbineRepository,
                windApplication().container.fylkeRepository,
            )
        }

        initializer {
            DetailsViewModel(
                windApplication().container.windTurbineRepository,
                this.createSavedStateHandle(),
            )
        }

        initializer {
            StationsViewModel(
                windApplication().container.fylkeRepository,
                windApplication().container.windTurbineRepository,
                windApplication().container.sourceRepository,
                windApplication().container.sourceHistoryRepository,
                this.createSavedStateHandle(),
            )
        }

        initializer {
            SettingsViewModel(
                windApplication().container.settingsRepository
            )
        }

        initializer {
            CountiesViewModel(
                windApplication().container.fylkeRepository,
//                inventoryApplication().container.windTurbineRepository,
            )
        }

    }
}

fun CreationExtras.windApplication(): WindInitializer =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as WindInitializer)
