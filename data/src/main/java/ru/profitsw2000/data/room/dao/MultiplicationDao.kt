package ru.profitsw2000.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity

@Dao
interface MultiplicationDao {

    @Query("SELECT * FROM MultiplicationHistoryEntity")
    fun all(): Single<List<MultiplicationHistoryEntity>>

    @Query("SELECT * FROM MultiplicationHistoryEntity WHERE id LIKE :testId")
    fun getMultiplicationTestById(testId: Int): Single<List<MultiplicationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(multiplicationHistoryEntity: MultiplicationHistoryEntity): Completable

    @Update
    fun update(multiplicationHistoryEntity: MultiplicationHistoryEntity): Completable

    @Delete
    fun delete(multiplicationHistoryEntity: MultiplicationHistoryEntity): Completable

}