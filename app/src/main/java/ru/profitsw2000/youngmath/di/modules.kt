package ru.profitsw2000.youngmath.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.profitsw2000.core.utils.SHARED_PREFERENCE_NAME
import ru.profitsw2000.data.room.database.AppDatabase

val appModule = module {
    single {
        AppDatabase.create(androidContext())
        AppDatabase.getInstance()
    }

    single {
        androidContext().getSharedPreferences(
            SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }
}