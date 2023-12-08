package ru.profitsw2000.data.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.profitsw2000.data.model.MultiplicationDataModel

private const val testTimeIncrementStep = 0.02f
private const val testTime = 10f
private const val testsNumber = 10

class MultiplicationTestDataGenerator(private val scope: CoroutineScope) {

    private var currentTestTime = 0f
    private var currentTestNumber = 1
    private var isStarted = false

    private var job: Job? = null
    private val mutableTestsDataSource: MutableStateFlow<MultiplicationDataModel> = MutableStateFlow(
        MultiplicationDataModel(currentTestTime, currentTestNumber)
    )
    val testsDataSource: StateFlow<MultiplicationDataModel> = mutableTestsDataSource

    fun startTest() {
        if (!isStarted) {
            isStarted = !isStarted
            job = scope.launch {
                while (isActive) {
                    incrementTime()
                    mutableTestsDataSource.value = MultiplicationDataModel(
                        currentTestTime,
                        currentTestNumber
                    )
                    delay((testTimeIncrementStep*1000).toLong())
                }
            }
        }
    }

    private fun incrementTime() {
        currentTestTime += testTimeIncrementStep
        if (isTestTimeOver()) {
            currentTestTime = 0f
            if (isLastTest()) {
                stopTest()
            } else {
                currentTestNumber++
            }
        }
    }

    private fun isTestTimeOver(): Boolean {
        return currentTestTime == testTime
    }

    private fun isLastTest(): Boolean {
        return currentTestNumber == testsNumber
    }

    private fun stopTest() {
        stopJob()
        clearTestData()
    }

    private fun stopJob() {
        job?.cancel()
        job = null
    }

    private fun clearTestData() {
        isStarted = !isStarted
        currentTestTime = 0f
        currentTestNumber = 1
    }
}