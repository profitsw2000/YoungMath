package ru.profitsw2000.data.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.profitsw2000.data.data.MultiplicationTestDataGenerator
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity

interface MultiplicationRepository {
    val multiplicationTestDataGenerator: MultiplicationTestDataGenerator

    fun writeMultiplicationTestResult(multiplicationHistoryEntity: MultiplicationHistoryEntity): Completable

    fun getMultiplicationTestResultsList(): Single<List<MultiplicationHistoryEntity>>

    fun getMultiplicationTestResultById(testId: Int): Single<MultiplicationHistoryEntity>

}