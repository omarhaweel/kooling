package com.example.vindmoller.data.source

import androidx.room.Embedded
import androidx.room.Relation
import com.example.vindmoller.data.sourcehistory.SourceHistory

data class SourceWithSourceHistory(
    @Embedded val source: Source,
    @Relation(
        parentColumn = "sourceId",
        entityColumn = "sourceId",
    )
    val sourceHistory: List<SourceHistory>
)