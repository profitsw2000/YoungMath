package ru.profitsw2000.multiplication.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.profitsw2000.core.utils.INTERRUPTED_MULTIPLICATION_TEST_DATE
import ru.profitsw2000.core.utils.MULTIPLICATION_FIVE_ASSESSMENT_ERRORS_NUMBER_KEY
import ru.profitsw2000.core.utils.MULTIPLICATION_FOUR_ASSESSMENT_ERRORS_NUMBER_KEY
import ru.profitsw2000.core.utils.MULTIPLICATION_HIGH_DIFFICULTY_FLAG_KEY
import ru.profitsw2000.core.utils.MULTIPLICATION_TASKS_NUMBER_KEY
import ru.profitsw2000.core.utils.MULTIPLICATION_TASK_TIME_KEY
import ru.profitsw2000.core.utils.MULTIPLICATION_TEST_IS_INTERRUPTED_FLAG_KEY
import ru.profitsw2000.core.utils.MULTIPLICATION_THREE_ASSESSMENT_ERRORS_NUMBER_KEY
import ru.profitsw2000.core.viewmodel.CoreViewModel
import ru.profitsw2000.data.domain.MultiplicationRepository
import ru.profitsw2000.data.mappers.MultiplicationHistoryMapper
import ru.profitsw2000.data.model.MultiplicationDataModel
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.data.model.MultiplicationTestSettingsModel
import ru.profitsw2000.data.model.state.MultiplicationHistoryState
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity
import java.util.Calendar
import java.util.Date

class MultiplicationViewModel (
    private val multiplicationRepository: MultiplicationRepository,
    private val multiplicationHistoryMapper: MultiplicationHistoryMapper,
    private val sharedPreferences: SharedPreferences
) : CoreViewModel() {

    var currentMultiplicationHistoryListVisiblePosition = 0

    private val startPosition = 0
    private val loadSize = 10
    private val startPageSize = 40
    private var currentMax = 0
    private var loadInProgress = false
    private var currentMultiplicationHistoryListBindPosition = 0
    private var historyTableSize = 0
    private val multiplicationHistoryModelList = mutableListOf<MultiplicationHistoryModel>()

    private var isTestAddedToDataBase = false

    val multiplicationTestLiveData: LiveData<MultiplicationDataModel> = multiplicationRepository.multiplicationTestDataGenerator.testsDataSource.asLiveData()
    val multiplicationHistoryResultsLiveData: LiveData<MultiplicationHistoryModel> = multiplicationRepository.multiplicationTestDataGenerator.multiplicationHistoryDataSource.asLiveData()

    private val _multiplicationHistoryLiveData = MutableLiveData<MultiplicationHistoryState>()
    val multiplicationHistoryLiveData: LiveData<MultiplicationHistoryState> by this::_multiplicationHistoryLiveData

    fun startTest() {
        multiplicationRepository.multiplicationTestDataGenerator.startTest()
        isTestAddedToDataBase = false
    }

    fun skipTest() {
        multiplicationRepository.multiplicationTestDataGenerator.skipTest()
    }

    fun nextTask(result: Int) {
        multiplicationRepository.multiplicationTestDataGenerator.nextTask(result)
    }

    fun writeMultiplicationTestResult(multiplicationHistoryModel: MultiplicationHistoryModel) {
        if (!isTestAddedToDataBase) {
            _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
            multiplicationRepository.writeMultiplicationTestResult(multiplicationHistoryMapper.map(multiplicationHistoryModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(
                            arrayListOf()
                        )
                        isTestAddedToDataBase = true
                        currentMultiplicationHistoryListVisiblePosition++
                    },{
                        val message = it.message ?: ""
                        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                    }
                )
        }
    }

    fun updateCurrentMultiplicationHistoryListPosition(position: Int) {
        currentMultiplicationHistoryListBindPosition = position
        if (currentMax - currentMultiplicationHistoryListBindPosition < loadSize &&
            !loadInProgress &&
            historyTableSize != multiplicationHistoryModelList.size) {
            loadNextPage()
        }
    }

    fun loadMultiplicationHistoryList() {
        setLoadingMultiplicationHistoryLiveData()
        multiplicationRepository.getMultiplicationHistoryListSize()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    checkMultiplicationHistoryTableIsEmpty(it)
                },
                {
                    val message = it.message ?: ""
                    setErrorMultiplicationHistoryLiveData(message)
                }
            )
    }

    private fun checkMultiplicationHistoryTableIsEmpty(tableSize: Int) {
        if (tableSize > 0) compareTableAndListSize(tableSize)
        else setSuccessMultiplicationHistoryLiveData()
    }

    private fun compareTableAndListSize(tableSize: Int) {
        if (tableSize != multiplicationHistoryModelList.size) compareWithPreviousTableSize(tableSize)
        else setSuccessMultiplicationHistoryLiveData()
    }

    private fun compareWithPreviousTableSize(tableSize: Int) {
        if (tableSize > historyTableSize) checkPreviousTableSize(tableSize)
        else setSuccessMultiplicationHistoryLiveData()
    }

    private fun checkPreviousTableSize(tableSize: Int) {
        if (historyTableSize == 0) {
            loadStartPage()
        } else {
            getNewElements(tableSize - historyTableSize, 0)
            currentMax += tableSize - historyTableSize
        }
        historyTableSize = tableSize
    }

    private fun loadStartPage() {
        multiplicationRepository.getMultiplicationHistoryPageList(startPageSize, startPosition)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    multiplicationHistoryModelList.addAll(multiplicationHistoryMapper.map(it))
                    setSuccessMultiplicationHistoryLiveData()
                    currentMax = multiplicationHistoryModelList.size
                },
                {
                    val message = it.message ?: ""
                    setErrorMultiplicationHistoryLiveData(message)
                }
            )
    }

    private fun getNewElements(elementsNumber: Int, offset: Int) {
        multiplicationRepository.getMultiplicationHistoryPageList(elementsNumber, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    multiplicationHistoryModelList.addAll(offset, multiplicationHistoryMapper.map(it))
                    setSuccessMultiplicationHistoryLiveData()
                },
                {
                    val message = it.message ?: ""
                    setErrorMultiplicationHistoryLiveData(message)
                }
            )
    }

    private fun loadNextPage() {
        setLoadingMultiplicationHistoryLiveData()
        multiplicationRepository.getMultiplicationHistoryPageList(loadSize, currentMax)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    multiplicationHistoryModelList.addAll(multiplicationHistoryMapper.map(it))
                    setSuccessMultiplicationHistoryLiveData()
                    currentMax += loadSize
                },
                {
                    val message = it.message ?: ""
                    setErrorMultiplicationHistoryLiveData(message)
                }
            )
    }

    private fun setSuccessMultiplicationHistoryLiveData() {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryModelList)
        loadInProgress = false
    }

    private fun setErrorMultiplicationHistoryLiveData(message: String) {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
        loadInProgress = false
    }

    private fun setLoadingMultiplicationHistoryLiveData() {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        loadInProgress = true
    }

    fun getMultiplicationSettings(): MultiplicationTestSettingsModel {
        return MultiplicationTestSettingsModel(
            taskTime = sharedPreferences.getFloat(MULTIPLICATION_TASK_TIME_KEY, 10f),
            tasksNumber = sharedPreferences.getInt(MULTIPLICATION_TASKS_NUMBER_KEY, 10),
            fiveAssessmentErrorsNumber = sharedPreferences.getInt(MULTIPLICATION_FIVE_ASSESSMENT_ERRORS_NUMBER_KEY, 0),
            fourAssessmentErrorsNumber = sharedPreferences.getInt(MULTIPLICATION_FOUR_ASSESSMENT_ERRORS_NUMBER_KEY, 1),
            threeAssessmentErrorsNumber = sharedPreferences.getInt(MULTIPLICATION_THREE_ASSESSMENT_ERRORS_NUMBER_KEY, 2),
            isHighDifficulty = sharedPreferences.getBoolean(MULTIPLICATION_HIGH_DIFFICULTY_FLAG_KEY, false)
        )
    }

    fun writeMultiplicationSettingsToSharedPreferences(multiplicationTestSettingsModel: MultiplicationTestSettingsModel) {
        with(multiplicationTestSettingsModel) {
            sharedPreferences
                .edit()
                .putFloat(MULTIPLICATION_TASK_TIME_KEY, taskTime)
                .putInt(MULTIPLICATION_TASKS_NUMBER_KEY, tasksNumber)
                .putInt(MULTIPLICATION_FIVE_ASSESSMENT_ERRORS_NUMBER_KEY, fiveAssessmentErrorsNumber)
                .putInt(MULTIPLICATION_FOUR_ASSESSMENT_ERRORS_NUMBER_KEY, fourAssessmentErrorsNumber)
                .putInt(MULTIPLICATION_THREE_ASSESSMENT_ERRORS_NUMBER_KEY, threeAssessmentErrorsNumber)
                .putBoolean(MULTIPLICATION_HIGH_DIFFICULTY_FLAG_KEY, isHighDifficulty)
                .apply()
        }
    }

    fun updateMultiplicationTestSettings(multiplicationTestSettingsModel: MultiplicationTestSettingsModel) {
        multiplicationRepository.multiplicationTestDataGenerator.updateSettings(multiplicationTestSettingsModel)
    }

    fun interruptTest() {
        val testDate: Long = Calendar.getInstance().timeInMillis

        sharedPreferences
            .edit()
            .putBoolean(MULTIPLICATION_TEST_IS_INTERRUPTED_FLAG_KEY, true)
            .putLong(INTERRUPTED_MULTIPLICATION_TEST_DATE, testDate)
            .apply()
    }

    fun resumeTest() {
        sharedPreferences
            .edit()
            .putBoolean(MULTIPLICATION_TEST_IS_INTERRUPTED_FLAG_KEY, false)
            .apply()
    }

    fun checkForInterruptedTest() {
        val testWasInterrupted = sharedPreferences.getBoolean(
            MULTIPLICATION_TEST_IS_INTERRUPTED_FLAG_KEY, false
        )

       if (testWasInterrupted) {
           writeMultiplicationTestResult(
               multiplicationHistoryMapper.map(
                   MultiplicationHistoryEntity(
                       id = 0,
                       Date(sharedPreferences.getLong(INTERRUPTED_MULTIPLICATION_TEST_DATE, 0)),
                       assessment = 1,
                       firstMultiplicatorList = arrayListOf(),
                       secondMultiplicatorList = arrayListOf(),
                       userMultiplicationResults = arrayListOf(),
                       tasksTime = arrayListOf(),
                       tasksNumber = sharedPreferences.getInt(MULTIPLICATION_TASKS_NUMBER_KEY, 10),
                       testWasInterrupted
                   )
               )
           )
           //сброс флага, сигнализирующего, что тест был прерван
           resumeTest()
       }
    }

}