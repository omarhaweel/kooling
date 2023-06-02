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

package com.example.vindmoller.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vindmoller.ui.screens.details.DetailsScreen
import com.example.vindmoller.ui.screens.stations.StationsScreen
import com.example.vindmoller.ui.screens.map.MapScreen
import com.example.vindmoller.ui.screens.home.HomeScreen
import com.example.vindmoller.ui.screens.settings.SettingsScreen
import com.example.vindmoller.ui.screens.counties.CountiesScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WindNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.HomeRoute.route
    ) {
        composable(route = NavigationRoutes.SettingsRoute.route) {
            SettingsScreen { navController.navigate(NavigationRoutes.HomeRoute.route) }
        }

        composable(route = NavigationRoutes.HomeRoute.route) {
            HomeScreen()
        }

        composable(route = NavigationRoutes.MapRoute.route) {
            MapScreen(
                navigateToSourceDetails = { navController.navigate("${NavigationRoutes.DetailsRoute.route}/${it}") }
            )
        }

        composable(route = NavigationRoutes.StatsRoute.route) {
            CountiesScreen(
                navigateToFylke = { navController.navigate("${NavigationRoutes.FylkeRoute.route}/${it}") },
            )
        }

        composable(
            route = "${NavigationRoutes.DetailsRoute.route}/{sourceId}",
            arguments = listOf(navArgument("sourceId") {
                type = NavType.StringType
            })
        ) {
            DetailsScreen(navigateBack = { navController.popBackStack() })
        }

        composable(
            route = "${NavigationRoutes.FylkeRoute.route}/{fylkeId}",
            arguments = listOf(navArgument("fylkeId") {
                type = NavType.IntType
            })
        ) {
            StationsScreen(navigateBack = { navController.popBackStack() })
        }
    }
}
