package ru.profitsw2000.data.room.entity

import java.util.Date

data class MultiplicationHistoryEntity(
    val testDate: Date,
    val firstMultiplicator: Int,
    val secondMultiplicator: Int,
    val userMathResult: Int,
    val tasksTime: List<Float>,
    val isInterrupted: Boolean
)
