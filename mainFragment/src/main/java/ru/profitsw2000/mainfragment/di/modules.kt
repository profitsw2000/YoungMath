package ru.profitsw2000.mainfragment.di

import org.koin.dsl.module
import ru.profitsw2000.data.data.MathCategoryRepositoryImpl
import ru.profitsw2000.data.domain.MathCategoryRepository
import ru.profitsw2000.mainfragment.presentation.viewmodel.MainViewModel

val mainModule = module {
    single { MainViewModel(get()) }
    single<MathCategoryRepository> { MathCategoryRepositoryImpl(get()) }
}