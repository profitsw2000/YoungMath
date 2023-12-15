package ru.profitsw2000.data.model

import android.drm.DrmRights
import java.util.Date

data class MultiplicationHistoryModel(
    val id: Int,
    val testDate: Date,
    val assessment: Int,
    val rightAnswers: Int,
    val wrongAnswers: Int,
    val firstMultiplicatorList: List<Int>,
    val secondMultiplicatorList: List<Int>,
    val multiplicationResults: List<Int>,
    val userMultiplicationResults: List<Int>,
    val tasksTime: List<Float>,
    val tasksNumber: Int,
    val testTime: Float,
    val results: List<Boolean>,
    val isInterrupted: Boolean
)
