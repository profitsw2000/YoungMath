package ru.profitsw2000.data.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.profitsw2000.data.domain.MultiplicationRepository
import ru.profitsw2000.data.room.database.AppDatabase
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity

class MultiplicationRepositoryImpl(
    private val database: AppDatabase
) : MultiplicationRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val multiplicationTestDataGenerator: MultiplicationTestDataGenerator = MultiplicationTestDataGenerator(coroutineScope)

    override fun writeMultiplicationTestResult(multiplicationHistoryEntity: MultiplicationHistoryEntity): Completable {
        return database.multiplicationDao.insert(multiplicationHistoryEntity)
    }

    override fun getMultiplicationTestResultsList(): Single<List<MultiplicationHistoryEntity>> {
        return database.multiplicationDao.all()
    }

    override fun getMultiplicationTestResultById(testId: Int): Single<MultiplicationHistoryEntity> {
        return database.multiplicationDao.getMultiplicationTestById(testId)
    }

    override fun getMultiplicationHistoryPageList(
        loadSize: Int,
        offset: Int
    ): Single<List<MultiplicationHistoryEntity>> {
        return database.multiplicationDao.getMultiplicationHistoryPageList(loadSize, offset)
    }
}