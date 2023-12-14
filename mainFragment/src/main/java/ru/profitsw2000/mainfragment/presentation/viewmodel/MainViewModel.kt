package ru.profitsw2000.mainfragment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.profitsw2000.core.viewmodel.CoreViewModel
import ru.profitsw2000.data.domain.MathCategoryRepository
import ru.profitsw2000.data.model.MathCategoryModel

class MainViewModel (
    private val mathCategoryRepository: MathCategoryRepository
) : CoreViewModel() {

    private val _categoryLiveData = MutableLiveData<List<MathCategoryModel>>()
    val categoryLiveData: LiveData<List<MathCategoryModel>> by this::_categoryLiveData

    fun getCategoryList() {
        _categoryLiveData.value = mathCategoryRepository.getMathCategoriesList()
    }
}