package ru.profitsw2000.multiplication.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import ru.profitsw2000.data.model.MultiplicationHistoryModel

class MultiplicationHistoryDiffUtilCallback(
    private val oldList: List<MultiplicationHistoryModel>,
    private val newList: List<MultiplicationHistoryModel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].assessment == newList[newItemPosition].assessment &&
                oldList[oldItemPosition].testTime == newList[newItemPosition].testTime &&
                oldList[oldItemPosition].testDate == newList[newItemPosition].testDate
    }
}