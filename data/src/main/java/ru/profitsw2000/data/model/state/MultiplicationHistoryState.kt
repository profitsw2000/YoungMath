package ru.profitsw2000.data.model.state

import ru.profitsw2000.data.model.MultiplicationHistoryModel

sealed class MultiplicationHistoryState{
    data class Success(val multiplicationHistoryModelList: List<MultiplicationHistoryModel>): MultiplicationHistoryState()
    data class Error(val message: String): MultiplicationHistoryState()
    object Loading: MultiplicationHistoryState()
}
