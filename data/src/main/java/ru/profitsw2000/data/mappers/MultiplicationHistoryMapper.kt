package ru.profitsw2000.data.mappers

import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity

class MultiplicationHistoryMapper {

    fun map(multiplicationHistoryModel: MultiplicationHistoryModel): MultiplicationHistoryEntity {
        return MultiplicationHistoryEntity(
            id = multiplicationHistoryModel.id,
            testDate = multiplicationHistoryModel.testDate,
            firstMultiplicatorList = multiplicationHistoryModel.firstMultiplicatorList,
            secondMultiplicatorList = multiplicationHistoryModel.secondMultiplicatorList,
            userMultiplicationResults = multiplicationHistoryModel.userMultiplicationResults,
            tasksTime = multiplicationHistoryModel.tasksTime,
            tasksNumber = multiplicationHistoryModel.tasksNumber,
            isInterrupted = multiplicationHistoryModel.isInterrupted
        )
    }

    fun map(multiplicationHistoryEntity: MultiplicationHistoryEntity): MultiplicationHistoryModel {
        return MultiplicationHistoryModel(
            id = multiplicationHistoryEntity.id,
            testDate = multiplicationHistoryEntity.testDate,
            assessment = getAssessment(
                multiplicationHistoryEntity.firstMultiplicatorList,
                multiplicationHistoryEntity.secondMultiplicatorList,
                multiplicationHistoryEntity.userMultiplicationResults,
                multiplicationHistoryEntity.tasksNumber
            ),
            rightAnswers = getRightAnswersNumber(
                multiplicationHistoryEntity.firstMultiplicatorList,
                multiplicationHistoryEntity.secondMultiplicatorList,
                multiplicationHistoryEntity.userMultiplicationResults,
            ),
            wrongAnswers = getWrongAnswersNumber(
                multiplicationHistoryEntity.firstMultiplicatorList,
                multiplicationHistoryEntity.secondMultiplicatorList,
                multiplicationHistoryEntity.userMultiplicationResults,
            ),
            firstMultiplicatorList = multiplicationHistoryEntity.firstMultiplicatorList,
            secondMultiplicatorList = multiplicationHistoryEntity.secondMultiplicatorList,
            multiplicationResults = getMultiplicationResults(multiplicationHistoryEntity.firstMultiplicatorList,
                multiplicationHistoryEntity.secondMultiplicatorList,
            ),
            userMultiplicationResults = multiplicationHistoryEntity.userMultiplicationResults,
            tasksTime = multiplicationHistoryEntity.tasksTime,
            tasksNumber = multiplicationHistoryEntity.tasksNumber,
            testTime = ,
            results = ,
            isInterrupted = multiplicationHistoryEntity.isInterrupted
        )
    }

    private fun getAssessment(firstMultiplicatorList: List<Int>,
                              secondMultiplicatorList: List<Int>,
                              multiplicationResults: List<Int>,
                              tasksNumber: Int): Int {
        val rightAnswers = getRightAnswersNumber(firstMultiplicatorList,
            secondMultiplicatorList,
            multiplicationResults)

        return when(tasksNumber - rightAnswers) {
            0 -> 5
            1 -> 4
            2 -> 3
            else -> 2
        }
    }

    private fun getRightAnswersNumber(firstMultiplicatorList: List<Int>,
                                      secondMultiplicatorList: List<Int>,
                                      multiplicationResults: List<Int>): Int {
        var rightAnswers = 0

        firstMultiplicatorList.forEachIndexed { index, i ->
            if (i*secondMultiplicatorList[index] == multiplicationResults[index]) rightAnswers++
        }
        return rightAnswers
    }

    private fun getWrongAnswersNumber(firstMultiplicatorList: List<Int>,
                                      secondMultiplicatorList: List<Int>,
                                      multiplicationResults: List<Int>): Int {
        var wrongAnswers = 0

        firstMultiplicatorList.forEachIndexed { index, i ->
            if (i*secondMultiplicatorList[index] != multiplicationResults[index]) wrongAnswers++
        }
        return wrongAnswers
    }

    private fun getMultiplicationResults(firstMultiplicatorList: List<Int>,
                                         secondMultiplicatorList: List<Int>): List<Int> {
        return mutableListOf<Int>().apply {
            firstMultiplicatorList.forEachIndexed { index, i ->
                this.add(i*secondMultiplicatorList[index])
            }
        }
    }
}