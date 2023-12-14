package ru.profitsw2000.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity

@Dao
interface MultiplicationDao {

    @Query("SELECT * FROM MultiplicationHistoryEntity")
    fun all(): Single<List<MultiplicationHistoryEntity>>

}