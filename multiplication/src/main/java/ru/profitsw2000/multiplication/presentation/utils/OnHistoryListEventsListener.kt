package ru.profitsw2000.multiplication.presentation.utils

import ru.profitsw2000.data.model.MultiplicationHistoryModel

interface OnHistoryListEventsListener {

    fun onItemClick(multiplicationHistoryModel: MultiplicationHistoryModel)

    fun onPositionChanged(position: Int)
}