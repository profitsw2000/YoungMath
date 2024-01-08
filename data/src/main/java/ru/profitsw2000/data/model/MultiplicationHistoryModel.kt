package ru.profitsw2000.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
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
    val isHighDifficulty: Boolean,
    val isInterrupted: Boolean
): Parcelable
