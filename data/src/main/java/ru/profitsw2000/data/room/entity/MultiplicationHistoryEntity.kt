package ru.profitsw2000.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class MultiplicationHistoryEntity(
    @PrimaryKey
    val id: Int,
    val testDate: Date,
    val firstMultiplicator: Int,
    val secondMultiplicator: Int,
    val userMathResult: Int,
    val tasksTime: List<Float>,
    val isInterrupted: Boolean
)
