package ru.profitsw2000.data.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.profitsw2000.data.domain.MultiplicationRepository

class MultiplicationRepositoryImpl : MultiplicationRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val multiplicationTestDataGenerator: MultiplicationTestDataGenerator = MultiplicationTestDataGenerator(coroutineScope)
}