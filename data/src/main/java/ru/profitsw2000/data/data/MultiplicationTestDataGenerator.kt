package ru.profitsw2000.data.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.profitsw2000.data.model.MultiplicationDataModel
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

private const val taskTimeIncrementStep = 0.02f
private const val taskTime = 10f
private const val tasksNumber = 10

class MultiplicationTestDataGenerator(private val scope: CoroutineScope) {

    //variables to write to database
    private var testDate = Calendar.getInstance().time
    private var assessment = 0
    private var rightAnswers = 0
    private var wrongAnswers = 0
    private val firstMultiplicatorList = mutableListOf<Int>()
    private val secondMultiplicatorList = mutableListOf<Int>()
    private val multiplicationResults = mutableListOf<Int>()
    private val userMultiplicationResults = mutableListOf<Int>()
    private val tasksTime = mutableListOf<Float>()
    private val testTime get() = getTestTotalTime(tasksTime)
    private val results = mutableListOf<Boolean>()
    private var isInterrupted = false

    private var currentTaskTime = 0f
    private var currentTaskNumber = 1
    private var firstMultiplier = 0
    private var secondMultiplier = 0
    private val taskResult get() = firstMultiplier*secondMultiplier
    private var userResult = 0
    private var isStarted = false
    private var resultsArray = Array(tasksNumber) {false}

    private var job: Job? = null
    private val mutableTestsDataSource: MutableStateFlow<MultiplicationDataModel> = MutableStateFlow(
        MultiplicationDataModel(currentTaskTime, currentTaskNumber, firstMultiplier, secondMultiplier, isStarted)
    )
    val testsDataSource: StateFlow<MultiplicationDataModel> = mutableTestsDataSource

    private val mutableResultsDataSource: MutableStateFlow<Array<Boolean>> = MutableStateFlow(resultsArray)
    val resultsDataSource: StateFlow<Array<Boolean>> = mutableResultsDataSource

    private val mutableMultiplicationHistoryDataSource: MutableStateFlow<MultiplicationHistoryModel> = MutableStateFlow(
        MultiplicationHistoryModel(id = 0,
            testDate = testDate,
            assessment = assessment,
            rightAnswers = rightAnswers,
            wrongAnswers = wrongAnswers,
            firstMultiplicatorList = firstMultiplicatorList,
            secondMultiplicatorList = secondMultiplicatorList,
            multiplicationResults = multiplicationResults,
            userMultiplicationResults = userMultiplicationResults,
            tasksTime = tasksTime,
            tasksNumber = tasksNumber,
            testTime = testTime,
            results = results,
            isInterrupted = isInterrupted
        )
    )
    val multiplicationHistoryDataSource: StateFlow<MultiplicationHistoryModel> = mutableMultiplicationHistoryDataSource

    fun startTest() {
        if (!isStarted) {
            isStarted = !isStarted
            resultsArray.fill(false)
            generateTaskData()
            initDbVariables()

            job = scope.launch {
                while (isActive) {
                    incrementTime()
                    mutableTestsDataSource.value = MultiplicationDataModel(
                        currentTaskTime,
                        currentTaskNumber,
                        firstMultiplier,
                        secondMultiplier,
                        isStarted
                    )
                    delay((taskTimeIncrementStep*1000).toLong())
                }
            }
        }
    }

    fun skipTest() {
        isInterrupted = true
        stopTest()
    }

    fun nextTask(result: Int) {
        resultsArray[currentTaskNumber - 1] = (result == taskResult)
        userResult = result
        updateDbVariables(result)
        if (isLastTest()) {
            stopTest()
        } else {
            incrementTaskNumber()
        }
    }

    private fun incrementTime() {
        currentTaskTime += taskTimeIncrementStep
        if (isTestTimeOver()) {
            nextTask(0)
        }
    }

    private fun incrementTaskNumber() {
        currentTaskTime = 0f
        currentTaskNumber++
        generateTaskData()
    }

    private fun isTestTimeOver(): Boolean {
        return currentTaskTime >= taskTime
    }

    private fun isLastTest(): Boolean {
        return currentTaskNumber == tasksNumber
    }

    private fun stopTest() {
        mutableResultsDataSource.value = resultsArray
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
        mutableTestsDataSource.value = MultiplicationDataModel(
            currentTaskTime,
            currentTaskNumber,
            firstMultiplier,
            secondMultiplier,
            isStarted
        )
    }

    private fun generateTaskData() {
        firstMultiplier = Random.nextInt(2, 10)
        secondMultiplier = Random.nextInt(2, 10)
    }

    private fun initDbVariables() {
        testDate = Calendar.getInstance().time
        assessment = 2
        rightAnswers = 0
        wrongAnswers = 0
        firstMultiplicatorList.clear()
        secondMultiplicatorList.clear()
        multiplicationResults.clear()
        userMultiplicationResults.clear()
        tasksTime.clear()
        testTime
        results.clear()
        isInterrupted = false
    }

    private fun updateDbVariables(result: Int) {
        if (result == taskResult) rightAnswers++
        else wrongAnswers++
        firstMultiplicatorList.add(firstMultiplier)
        secondMultiplicatorList.add(secondMultiplier)
        multiplicationResults.add(taskResult)
        userMultiplicationResults.add(result)
        tasksTime.add(taskTime)
        results.add(taskResult == result)
    }

    private fun writeDbVariables() {
        mutableMultiplicationHistoryDataSource.value = MultiplicationHistoryModel(
            id = 0,
            testDate = testDate,
            assessment = assessment,
            rightAnswers = rightAnswers,
            wrongAnswers = wrongAnswers,
            firstMultiplicatorList = firstMultiplicatorList,
            secondMultiplicatorList = secondMultiplicatorList,
            multiplicationResults = multiplicationResults,
            userMultiplicationResults = userMultiplicationResults,
            tasksTime = tasksTime,
            tasksNumber = tasksNumber,
            testTime = testTime,
            results = results,
            isInterrupted = isInterrupted
        )
    }

    private fun getTestTotalTime(timeList: List<Float>): Float {
        var totalTime = 0f

        tasksTime.forEach {
            totalTime += it
        }
        return totalTime
    }
}