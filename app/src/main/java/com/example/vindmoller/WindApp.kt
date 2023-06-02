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
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vindmoller.ui.navigation.WindNavHost
import com.example.vindmoller.ui.navigation.NavigationRoutes
import com.example.vindmoller.ui.theme.navBarGradient

val routes = listOf(
    NavigationRoutes.MapRoute,
    NavigationRoutes.HomeRoute,
    NavigationRoutes.StatsRoute,
    NavigationRoutes.SettingsRoute
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WindApp(windowSizeClass: WindowSizeClass, navController: NavHostController = rememberNavController()) {
    if(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        Scaffold(
            bottomBar = { // will be located in the bottom
                WindBottomBar(navController)
            }
        ) { // this is the main content above the navbar
            Box(modifier = Modifier.padding(it)) {
                WindNavHost(navController = navController)
            }
        }

    } else {
        // for some reason they haven't added a place to add NavigationRail in Scaffold
        // also for some reason the theme isn't applied if the content isn't inside a Scaffold
        Scaffold {
            Row {
                WindRailBar(navController)
                Box(modifier = Modifier.padding(it)) {
                    WindNavHost(navController = navController)
                }
            }
        }

    }

}

@Composable
fun WindBottomBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
            .background(navBarGradient.value)
    ) {
        routes.forEach { route ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(route.icon),
                        contentDescription = route.route, modifier = Modifier.size(25.dp)
                    )
                },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == route.route,
                onClick = {
                    navController.navigate(route.route)
                }
            )
        }
    }
}

@Composable
fun WindRailBar(navController: NavHostController) {
    NavigationRail(
        containerColor = Color.Transparent,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
            .background(navBarGradient.value)
    ) {
        Spacer(Modifier.weight(1f))

        routes.forEach { route ->
            NavigationRailItem(
                icon = {
                    Icon(
                        painter = painterResource(route.icon),
                        contentDescription = route.route, modifier = Modifier.size(25.dp)
                    )
                },
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == route.route,
                onClick = {
                    navController.navigate(route.route)
                }
            )

            Spacer(Modifier.weight(1f))
        }
    }
}