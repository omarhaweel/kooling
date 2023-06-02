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

package com.example.vindmoller.data

import com.example.vindmoller.data.composite.CompositeRepository
import com.example.vindmoller.data.settings.SettingsRepository
import com.example.vindmoller.data.fylke.FylkeRepository
import com.example.vindmoller.data.point.PointRepository
import com.example.vindmoller.data.source.SourceRepository
import com.example.vindmoller.data.sourcehistory.SourceHistoryRepository
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import com.example.vindmoller.network.frost.FrostApiRepository
import com.example.vindmoller.network.locationforecast.LocationForecastApiRepository

interface AppContainer {
    val compositeRepository: CompositeRepository
    val settingsRepository: SettingsRepository
    val pointRepository: PointRepository
    val sourceRepository: SourceRepository
    val sourceHistoryRepository : SourceHistoryRepository
    val windTurbineRepository : WindTurbineRepository
    val fylkeRepository : FylkeRepository
    val locationForecastApiRepository : LocationForecastApiRepository
    val frostApiRepository : FrostApiRepository
    suspend fun resetDatabase()
    suspend fun launchDummyState()
}