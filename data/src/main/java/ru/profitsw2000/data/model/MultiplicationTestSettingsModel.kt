package ru.profitsw2000.data.model

data class MultiplicationTestSettingsModel(
    val taskTime: Float,
    val tasksNumber: Int,
    val fiveAssessmentErrorsNumber: Int,
    val fourAssessmentErrorsNumber: Int,
    val threeAssessmentErrorsNumber: Int,
    val isHighDifficulty: Boolean
)
