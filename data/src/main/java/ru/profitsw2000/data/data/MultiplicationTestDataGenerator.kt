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
import ru.profitsw2000.data.model.MultiplicationTestSettingsModel
import java.util.Calendar
import kotlin.random.Random

private const val taskTimeIncrementStep = 0.02f

class MultiplicationTestDataGenerator(private val scope: CoroutineScope) {

    //settings
    private var taskTime = 10f
    private var tasksNumber = 10
    private var fiveAssessmentErrorNumber = 0
    private var fourAssessmentErrorNumber = 1
    private var threeAssessmentErrorNumber = 2
    private var isHighDifficulty = false

    //variables to write to database
    private var testDate = Calendar.getInstance().time
    private val assessment get() = testAssessment()
    private var rightAnswers = 0
    private var wrongAnswers = 0
    private val firstMultiplicatorList = mutableListOf<Int>()
    private val secondMultiplicatorList = mutableListOf<Int>()
    private val multiplicationResults = mutableListOf<Int>()
    private val userMultiplicationResults = mutableListOf<Int>()
    private val tasksTime = mutableListOf<Float>()
    private val testTime get() = getTestTotalTime()
    private val results = mutableListOf<Boolean>()
    private var isInterrupted = false

    private var currentTaskTime = 0f
    private var currentTaskNumber = 1
    private var firstMultiplier = 0
    private var secondMultiplier = 0
    private val taskResult get() = firstMultiplier*secondMultiplier
    private var userResult = 0
    private var isStarted = false

    private var job: Job? = null
    private val mutableTestsDataSource: MutableStateFlow<MultiplicationDataModel> = MutableStateFlow(
        MultiplicationDataModel(currentTaskTime, currentTaskNumber, firstMultiplier, secondMultiplier, isStarted)
    )
    val testsDataSource: StateFlow<MultiplicationDataModel> = mutableTestsDataSource

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
    val multiplicationHistoryDataSource: StateFlow<MultiplicationHistoryModel> by this::mutableMultiplicationHistoryDataSource

    fun startTest() {
        if (!isStarted) {
            isStarted = true
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
        updateDbVariables(0)
        stopTest()
    }

    fun nextTask(result: Int) {
        userResult = result
        updateDbVariables(result)
        if (isLastTest()) {
            stopTest()
        } else {
            incrementTaskNumber()
        }
    }

    fun updateSettings(multiplicationTestSettingsModel: MultiplicationTestSettingsModel): Boolean {
        val fiveErrorNumber = multiplicationTestSettingsModel.fiveAssessmentErrorsNumber
        val fourErrorNumber = multiplicationTestSettingsModel.fourAssessmentErrorsNumber
        val threeErrorNumber = multiplicationTestSettingsModel.threeAssessmentErrorsNumber

        if (fourErrorNumber in (fiveErrorNumber + 1) until threeErrorNumber) {
            taskTime = multiplicationTestSettingsModel.taskTime
            tasksNumber = multiplicationTestSettingsModel.tasksNumber
            fiveAssessmentErrorNumber = fiveErrorNumber
            fourAssessmentErrorNumber = fourErrorNumber
            threeAssessmentErrorNumber = threeErrorNumber
            isHighDifficulty = multiplicationTestSettingsModel.isHighDifficulty
            return true
        } else return false
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
        writeDbVariables()
        stopJob()
        clearTestData()
    }

    private fun stopJob() {
        job?.cancel()
        job = null
    }

    private fun clearTestData() {
        isStarted = false
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
        tasksTime.add(currentTaskTime)
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

    private fun getTestTotalTime(): Float {
        var totalTime = 0f

        tasksTime.forEach {
            totalTime += it
        }
        return totalTime
    }

    private fun testAssessment(): Int {
        return when {
            (tasksNumber - rightAnswers) <= fiveAssessmentErrorNumber -> 5
            (tasksNumber - rightAnswers) > fiveAssessmentErrorNumber && (tasksNumber - rightAnswers) <= fourAssessmentErrorNumber-> 4
            (tasksNumber - rightAnswers) > fourAssessmentErrorNumber && (tasksNumber - rightAnswers) <= threeAssessmentErrorNumber -> 3
            else -> 2
        }
    }
}