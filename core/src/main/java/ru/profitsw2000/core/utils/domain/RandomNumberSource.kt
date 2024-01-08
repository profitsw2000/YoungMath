package ru.profitsw2000.core.utils.domain

interface RandomNumberSource {

    fun getSimpleRandomIntNumber(from: Int, to: Int): Int

    fun getBiasedRandomIntNumber(from: Int, to: Int): Int
}