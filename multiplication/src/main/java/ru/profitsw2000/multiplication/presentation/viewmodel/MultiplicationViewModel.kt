package ru.profitsw2000.multiplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.profitsw2000.data.domain.MultiplicationRepository
import ru.profitsw2000.data.model.MultiplicationDataModel

class MultiplicationViewModel (
    private val multiplicationRepository: MultiplicationRepository
) {

    private val multiplicationTestLiveData: LiveData<MultiplicationDataModel> = multiplicationRepository.multiplicationTestDataGenerator.testsDataSource.asLiveData()

    fun startTest() {
        multiplicationRepository.multiplicationTestDataGenerator.startTest()
    }
}