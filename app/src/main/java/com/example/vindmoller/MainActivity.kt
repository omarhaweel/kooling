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
package com.example.vindmoller

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.vindmoller.ui.theme.WindTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {

    private var timer: CountDownTimer? = null
//    lateinit var container: AppContainer
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkMode = remember { mutableStateOf(false) }
            val overrideTheme = remember { mutableStateOf(false) }

            LaunchedEffect(WindInitializer.db.settingsRepository) {
                WindInitializer.db.settingsRepository
                    .getValue("darkMode")
                    .collect { value ->
                        isDarkMode.value = value ?: false
                    }
            }

            LaunchedEffect(WindInitializer.db.settingsRepository) {
                WindInitializer.db.settingsRepository
                    .getValue("overrideSystemTheme")
                    .collect { value ->
                        overrideTheme.value = value ?: false
                    }
            }

            val windowSizeClass = calculateWindowSizeClass(this)

            WindTheme(
                darkTheme = if(overrideTheme.value) isDarkMode.value else isSystemInDarkTheme()
            ) {
                WindApp(windowSizeClass)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        timer = object : CountDownTimer(1000 * 60 * 60, 1000 * 60 * 10) {
            override fun onTick(millisUntilFinished: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        WindInitializer.db.compositeRepository.updateMissing()
                    }
                    catch (_: IOException) {}
                    catch (_: HttpException) {}
                }
            }

            override fun onFinish() {
                this.cancel()
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        timer = null
    }
}

