package ru.profitsw2000.multiplication.di

import org.koin.dsl.module
import ru.profitsw2000.data.data.MultiplicationRepositoryImpl
import ru.profitsw2000.data.domain.MultiplicationRepository
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel

val multiplicationModule = module {
    single<MultiplicationRepository> { MultiplicationRepositoryImpl() }
    single { MultiplicationViewModel(get()) }
}