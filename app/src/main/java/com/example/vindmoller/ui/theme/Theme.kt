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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = darkPrimaryColor,
    secondary = darkSecondaryColor,
    tertiary = darkTertiaryColor,
    background = darkBackgroundColor,
    surface = Color(0xFFCBE0E7),
    surfaceVariant = Color(0xFFCBE0E7),
    onPrimary = Color.White,
    //onSecondary = Color.White,
    //onTertiary = Color.White,
    //onBackground = Color.White,
    onSurface = Color(0xFF162121),
    //primaryContainer = Color.Green,
    //inverseOnSurface = Color.Green,
    onSurfaceVariant = Color.White
)

private val LightColorPalette = lightColorScheme(
    primary = darkPrimaryColor,
    secondary = darkSecondaryColor,
    tertiary = darkTertiaryColor,
    //background = Color(30,30,30)

)

@Composable
fun WindTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors: ColorScheme

    if (darkTheme) {
        colors = DarkColorPalette
        navBarGradient.value = darkNavBarGradient
        systemUiController.setStatusBarColor(darkBackgroundColor)
    } else {
        colors = LightColorPalette
        navBarGradient.value = lightNavBarGradient
        systemUiController.setStatusBarColor(Color.White)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
