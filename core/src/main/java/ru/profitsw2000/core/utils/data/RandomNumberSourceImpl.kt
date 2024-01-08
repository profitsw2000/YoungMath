package ru.profitsw2000.core.utils.data

import ru.profitsw2000.core.utils.domain.RandomNumberSource
import kotlin.random.Random

class RandomNumberSourceImpl: RandomNumberSource {
    override fun getSimpleRandomIntNumber(from: Int, to: Int): Int {
        return Random.nextInt(from, to)
    }

    override fun getBiasedRandomIntNumber(from: Int, to: Int): Int {
        val rangesNumber = (to - from)/3
        val randomRange = Random.nextInt(0, rangesNumber + 1)

        return Random.nextInt(randomRange*3 + from, to)
    }
}