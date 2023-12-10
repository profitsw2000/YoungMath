package ru.profitsw2000.data.domain

import ru.profitsw2000.data.data.MultiplicationTestDataGenerator

interface MultiplicationRepository {
    val multiplicationTestDataGenerator: MultiplicationTestDataGenerator
}