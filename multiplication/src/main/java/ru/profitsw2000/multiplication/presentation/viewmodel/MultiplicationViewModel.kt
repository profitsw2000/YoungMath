package ru.profitsw2000.multiplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.profitsw2000.core.viewmodel.CoreViewModel
import ru.profitsw2000.data.domain.MultiplicationRepository
import ru.profitsw2000.data.mappers.MultiplicationHistoryMapper
import ru.profitsw2000.data.model.MultiplicationDataModel
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.data.model.state.MultiplicationHistoryState

class MultiplicationViewModel (
    private val multiplicationRepository: MultiplicationRepository,
    private val multiplicationHistoryMapper: MultiplicationHistoryMapper
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
}