package com.example.vindmoller.util

import kotlinx.datetime.Instant

object Datetime {
    fun currentTimeRangeEnd(): Instant {
        val currentEpochSeconds = System.currentTimeMillis() / 1_000
        val nearestTenthMinuteEpochSeconds = (currentEpochSeconds / 600) * 600
        return Instant.fromEpochSeconds(
            nearestTenthMinuteEpochSeconds + 600,
            0
        )
    }

}