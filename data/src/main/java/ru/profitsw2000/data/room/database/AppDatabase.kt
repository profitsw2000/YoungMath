package ru.profitsw2000.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.profitsw2000.data.room.dao.MultiplicationDao
import ru.profitsw2000.data.room.entity.MultiplicationHistoryEntity

@Database(
    entities = [MultiplicationHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val multiplicationDao: MultiplicationDao

    companion object{
        private const val DB_NAME = "young_math.db"
        private var instance: AppDatabase? = null
        fun getInstance() = instance
            ?: throw java.lang.RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}