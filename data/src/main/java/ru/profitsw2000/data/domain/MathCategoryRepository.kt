package ru.profitsw2000.data.domain

import ru.profitsw2000.data.model.MathCategoryModel

interface MathCategoryRepository {

    fun getMathCategoriesList(): List<MathCategoryModel>

}