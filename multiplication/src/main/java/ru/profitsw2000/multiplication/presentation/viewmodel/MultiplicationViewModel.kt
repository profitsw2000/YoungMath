package ru.profitsw2000.multiplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.profitsw2000.core.viewmodel.CoreViewModel
import ru.profitsw2000.data.domain.MultiplicationRepository
import ru.profitsw2000.data.model.MultiplicationDataModel

class MultiplicationViewModel (
    private val multiplicationRepository: MultiplicationRepository
) : CoreViewModel() {

    val multiplicationTestLiveData: LiveData<MultiplicationDataModel> = multiplicationRepository.multiplicationTestDataGenerator.testsDataSource.asLiveData()
    val multiplicationTestResultsLiveData: LiveData<Array<Boolean>> = multiplicationRepository.multiplicationTestDataGenerator.resultsDataSource.asLiveData()

    fun startTest() {
        multiplicationRepository.multiplicationTestDataGenerator.startTest()
    }

    fun nextTask(result: Int) {
        multiplicationRepository.multiplicationTestDataGenerator.nextTask(result)
    }
}