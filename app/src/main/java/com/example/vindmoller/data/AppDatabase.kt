/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vindmoller.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vindmoller.data.fylke.Fylke
import com.example.vindmoller.data.fylke.FylkeDao
import com.example.vindmoller.data.point.Point
import com.example.vindmoller.data.point.PointDao
import com.example.vindmoller.data.settings.Settings
import com.example.vindmoller.data.settings.SettingsDao
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.source.SourceDao
import com.example.vindmoller.data.sourcehistory.SourceHistory
import com.example.vindmoller.data.sourcehistory.SourceHistoryDao
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.data.windturbines.WindTurbineDao

/**
 * Database class with a singleton INSTANCE object.
 */
@Database(
    version = 11,
    exportSchema = false,
    entities = [
        // static tables
        Fylke::class,       // maybe implement as typeconverter https://developer.android.com/training/data-storage/room/referencing-data #TODO
        Point::class,       // maybe clasisfy by view. county / national #TODO
        Source::class,      // maybe fetch dynamically as wind-logging stations may be added / retired
        // dynamic tables
        SourceHistory::class,
        WindTurbine::class,
        // persistant data table
        Settings::class,
    ]
)
@TypeConverters(DatabaseTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao
    abstract fun pointDao(): PointDao
    abstract fun sourceDao(): SourceDao
    abstract fun sourceHistoryDao(): SourceHistoryDao
    abstract fun windTurbineDao(): WindTurbineDao
    abstract fun fylkeDao(): FylkeDao
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    //.fallbackToDestructiveMigration()
                    .createFromAsset("defaultDatabase11.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
