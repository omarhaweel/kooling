package com.example.vindmoller.data


import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class DatabaseTypeConverter {
    @TypeConverter
    fun epochSecondsToInstant(timestamp: Long): Instant {
        return Instant.fromEpochSeconds(timestamp)
    }
    @TypeConverter
    fun instantToEpochSeconds(instant: Instant): Long {
        return instant.epochSeconds
    }
}