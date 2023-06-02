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
package com.example.vindmoller.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// dark mode colors
val darkPrimaryColor = Color(0xFF0D7798)
val darkSecondaryColor = Color(0xFF282E33)
val darkTertiaryColor = Color(0xFF03DAC5)
val darkBackgroundColor = Color(0xFF1E1E1E)

// light mode colors
// TODO

// others
val gradientColor1 = Color(0xFF1B3637)
val gradientColor2 = Color(0xFF0D7798)
val gradientColor3 = Color(0xFFCADFE6)
val gradientColor4 = Color(0xFF003645)

val darkNavBarGradient = Brush.linearGradient(
    colors = listOf(
        gradientColor1,
        gradientColor2
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)

val lightNavBarGradient = Brush.linearGradient(
    colors = listOf(
        gradientColor2,
        gradientColor3
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)

var navBarGradient = mutableStateOf(lightNavBarGradient)