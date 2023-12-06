package ru.profitsw2000.data.data

import android.content.Context
import ru.profitsw2000.data.domain.MathCategoryRepository
import ru.profitsw2000.data.model.MathCategoryModel

class MathCategoryRepositoryImpl(
    private val context: Context
): MathCategoryRepository {

    private val mathCategoryList = arrayListOf(
        MathCategoryModel(1, context.getString(ru.profitsw2000.core.R.string.multiplication_table_category_title))
    )

    override fun getMathCategoriesList(): List<MathCategoryModel> {
        return mathCategoryList
    }

}