package ru.profitsw2000.data.model

import android.drm.DrmRights
import java.util.Date

data class MultiplicationHistoryModel(
    val testDate: Date,
    val assessment: Int,
    val rightAnswers: Int,
    val wrongAnswers: Int,
    val firstMultiplicator: Int,
    val secondMultiplicator: Int,
    val multiplicationResult: List<Int>,
    val usermultiplicationResult: Int,
    val tasksTime: List<Float>,
    val testTime: Float,
    val results: List<Boolean>,
    val isInterrupted: Boolean
)
