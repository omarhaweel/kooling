package com.example.vindmoller.data

import android.content.Context
import com.example.vindmoller.data.composite.CompositeRepository
import com.example.vindmoller.data.composite.CompositeRepositoryImpl
import com.example.vindmoller.data.fylke.FylkeRepository
import com.example.vindmoller.data.fylke.FylkeRepositoryImpl
import com.example.vindmoller.data.point.PointRepository
import com.example.vindmoller.data.point.PointRepositoryImpl
import com.example.vindmoller.data.settings.SettingsRepository
import com.example.vindmoller.data.settings.SettingsRepositoryImpl
import com.example.vindmoller.data.source.SourceRepository
import com.example.vindmoller.data.source.SourceRepositoryImpl
import com.example.vindmoller.data.sourcehistory.SourceHistoryRepository
import com.example.vindmoller.data.sourcehistory.SourceHistoryRepositoryImpl
import com.example.vindmoller.data.windturbines.WindTurbineRepository
import com.example.vindmoller.data.windturbines.WindTurbineRepositoryImpl
import com.example.vindmoller.network.frost.FrostApi
import com.example.vindmoller.network.frost.FrostApiRepository
import com.example.vindmoller.network.frost.FrostApiRepositoryImpl
import com.example.vindmoller.network.locationforecast.LocationForecastApi
import com.example.vindmoller.network.locationforecast.LocationForecastApiRepository
import com.example.vindmoller.network.locationforecast.LocationForecastApiRepositoryImpl

class AppDataImpl(private val context: Context) : AppContainer {
    override val compositeRepository: CompositeRepository by lazy {
        CompositeRepositoryImpl(
            sourceHistoryRepository,
            windTurbineRepository,
            frostApiRepository,
            fylkeRepository
        )
    }

    override val locationForecastApiRepository: LocationForecastApiRepository by lazy {
        LocationForecastApiRepositoryImpl(LocationForecastApi.getService())
    }

    override val frostApiRepository: FrostApiRepository by lazy {
        FrostApiRepositoryImpl(FrostApi.getService())
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(AppDatabase.getDatabase(context).settingsDao())
    }

    override val fylkeRepository: FylkeRepository by lazy {
        FylkeRepositoryImpl(AppDatabase.getDatabase(context).fylkeDao())
    }

    override val pointRepository: PointRepository by lazy {
        PointRepositoryImpl(AppDatabase.getDatabase(context).pointDao())
    }

    override val sourceRepository: SourceRepository by lazy {
        SourceRepositoryImpl(AppDatabase.getDatabase((context)).sourceDao())
    }

    override val sourceHistoryRepository: SourceHistoryRepository by lazy {
        SourceHistoryRepositoryImpl(AppDatabase.getDatabase((context)).sourceHistoryDao())
    }

    override val windTurbineRepository: WindTurbineRepository by lazy {
        WindTurbineRepositoryImpl(AppDatabase.getDatabase((context)).windTurbineDao())
    }

    override suspend fun resetDatabase() {
        compositeRepository.resetWind()
    }

    override suspend fun launchDummyState() {
        compositeRepository.launchDummyState()
    }
}