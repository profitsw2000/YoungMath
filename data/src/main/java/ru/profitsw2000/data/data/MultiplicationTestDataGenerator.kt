package ru.profitsw2000.data.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.profitsw2000.data.model.MultiplicationDataModel
import kotlin.random.Random

private const val taskTimeIncrementStep = 0.02f
private const val taskTime = 10f
private const val tasksNumber = 10

class MultiplicationTestDataGenerator(private val scope: CoroutineScope) {

    private var currentTaskTime = 0f
    private var currentTaskNumber = 1
    private var firstMultiplier = 0
    private var secondMultiplier = 0
    private var taskResult = firstMultiplier*secondMultiplier
    private var userResult = 0
    private var isStarted = false

    private var job: Job? = null
    private val mutableTestsDataSource: MutableStateFlow<MultiplicationDataModel> = MutableStateFlow(
        MultiplicationDataModel(currentTaskTime, currentTaskNumber, firstMultiplier, secondMultiplier)
    )
    val testsDataSource: StateFlow<MultiplicationDataModel> = mutableTestsDataSource

    fun startTest() {
        if (!isStarted) {
            isStarted = !isStarted
            generateTaskData()
            job = scope.launch {
                while (isActive) {
                    incrementTime()
                    mutableTestsDataSource.value = MultiplicationDataModel(
                        currentTaskTime,
                        currentTaskNumber,
                        firstMultiplier,
                        secondMultiplier
                    )
                    delay((taskTimeIncrementStep*1000).toLong())
                }
            }
        }
    }

    fun nextTask(result: Int) {
        currentTaskTime = 0f
        currentTaskNumber++
        userResult = result
        generateTaskData()
    }

    private fun incrementTime() {
        currentTaskTime += taskTimeIncrementStep
        if (isTestTimeOver()) {
            currentTaskTime = 0f
            if (isLastTest()) {
                stopTest()
            } else {
                generateTaskData()
                currentTaskNumber++
            }
        }
    }

    private fun isTestTimeOver(): Boolean {
        return currentTaskTime >= taskTime
    }

    private fun isLastTest(): Boolean {
        return currentTaskNumber == tasksNumber
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
        currentTaskTime = 0f
        currentTaskNumber = 1
    }

    private fun generateTaskData() {
        firstMultiplier = Random.nextInt(2, 10)
        secondMultiplier = Random.nextInt(2, 10)
    }
}