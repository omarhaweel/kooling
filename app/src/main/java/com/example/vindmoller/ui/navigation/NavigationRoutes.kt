package com.example.vindmoller.ui.navigation

import com.example.vindmoller.R
// routes to different screens
sealed class NavigationRoutes(val route: String, val icon: Int) {
    object MapRoute : NavigationRoutes("map", R.drawable.map_icon)
    object HomeRoute : NavigationRoutes("home", R.drawable.home_icon)
    object StatsRoute : NavigationRoutes("stats", R.drawable.stats_icon)
    object DetailsRoute : NavigationRoutes("details", R.drawable.stats_icon)
    object FylkeRoute : NavigationRoutes("fylke", R.drawable.stats_icon)
    object SettingsRoute : NavigationRoutes("settings", R.drawable.settings_icon )
}


