package ru.profitsw2000.multiplication.presentation.viewmodel

import android.util.Log
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

    private val startPosition = 0
    private val loadSize = 10
    private val startPageSize = 40
    private var currentMax = startPageSize
    private var loadInProgress = false
    private var currentMultiplicationHistoryListBindPosition = 0
    private var historyTableSize = 0
    private val multiplicationHistoryModelList = mutableListOf<MultiplicationHistoryModel>()

    val multiplicationTestLiveData: LiveData<MultiplicationDataModel> = multiplicationRepository.multiplicationTestDataGenerator.testsDataSource.asLiveData()
    val multiplicationHistoryResultsLiveData: LiveData<MultiplicationHistoryModel> = multiplicationRepository.multiplicationTestDataGenerator.multiplicationHistoryDataSource.asLiveData()

    private val _multiplicationHistoryLiveData = MutableLiveData<MultiplicationHistoryState>()
    val multiplicationHistoryLiveData: LiveData<MultiplicationHistoryState> by this::_multiplicationHistoryLiveData

    fun startTest() {
        multiplicationRepository.multiplicationTestDataGenerator.startTest()
    }

    fun skipTest() {
        multiplicationRepository.multiplicationTestDataGenerator.skipTest()
    }

    fun nextTask(result: Int) {
        multiplicationRepository.multiplicationTestDataGenerator.nextTask(result)
    }

    fun writeMultiplicationTestResult(multiplicationHistoryModel: MultiplicationHistoryModel) {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        multiplicationRepository.writeMultiplicationTestResult(multiplicationHistoryMapper.map(multiplicationHistoryModel))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(
                        arrayListOf()
                    )
                },{
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                }
            )
    }

    fun getMultiplicationTestResultsList() {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        multiplicationRepository.getMultiplicationTestResultsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryMapper.map(it))
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                }
            )
    }

    fun getMultiplicationTestResultsListById(testId: Int) {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        multiplicationRepository.getMultiplicationTestResultById(testId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryMapper.map(
                        arrayListOf(it)
                    ))
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                }
            )
    }

    fun updateCurrentMultiplicationHistoryListPosition(position: Int) {
        currentMultiplicationHistoryListBindPosition = position
        if (currentMax - currentMultiplicationHistoryListBindPosition < loadSize &&
            !loadInProgress) {
            loadNextPage()
        }
    }

    fun loadFirstPage() {
        if (multiplicationHistoryModelList.size == 0) {
            _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
            multiplicationRepository.getMultiplicationHistoryPageList(startPageSize, startPosition)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        multiplicationHistoryModelList.addAll(multiplicationHistoryMapper.map(it))
                        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryModelList)
                        historyTableSize = multiplicationHistoryModelList.size + 1
                    },
                    {
                        val message = it.message ?: ""
                        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                    }
                )
        } else {
            _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
            getMultiplicationHistoryList()
            loadInProgress = true
        }
    }

    fun loadNextPage() {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        loadInProgress = true
        multiplicationRepository.getMultiplicationHistoryPageList(loadSize, currentMax)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    multiplicationHistoryModelList.addAll(multiplicationHistoryMapper.map(it))
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryModelList)
                    currentMax += loadSize
                    loadInProgress = false
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                    loadInProgress = false
                }
            )
    }

    private fun getMultiplicationHistoryList() {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        multiplicationRepository.getMultiplicationHistoryListSize()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    historyTableSize = it
                    if (it == multiplicationHistoryModelList.size) {
                        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryModelList)
                    } else {
                        getNewElements((it - multiplicationHistoryModelList.size), 0)
                    }
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                    loadInProgress = false
                }
            )
    }

    private fun getNewElements(elementsNumber: Int, offset: Int) {
        multiplicationRepository.getMultiplicationHistoryPageList(elementsNumber, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    multiplicationHistoryModelList.addAll(0, multiplicationHistoryMapper.map(it))
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryModelList)
                    loadInProgress = false
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                    loadInProgress = false
                }
            )
    }

    fun loadMultiplicationHistoryList() {
        _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Loading
        loadInProgress = true
        multiplicationRepository.getMultiplicationHistoryListSize()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    checkMultiplicationHistoryTableIsEmpty(it)
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                    loadInProgress = false
                }
            )
    }

    private fun checkMultiplicationHistoryTableIsEmpty(tableSize: Int) {
        if (tableSize > 0) compareTableAndListSize(tableSize)
    }

    private fun compareTableAndListSize(tableSize: Int) {
        if (tableSize != multiplicationHistoryModelList.size) compareWithPreviousTableSize(tableSize)
    }

    private fun compareWithPreviousTableSize(tableSize: Int) {
        if (tableSize > historyTableSize) checkPreviousTableSize(tableSize)
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
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Success(multiplicationHistoryModelList)
                    currentMax = multiplicationHistoryModelList.size
                },
                {
                    val message = it.message ?: ""
                    _multiplicationHistoryLiveData.value = MultiplicationHistoryState.Error(message)
                }
            )
    }
}