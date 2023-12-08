package ru.profitsw2000.youngmath.navigation

import androidx.navigation.NavController
import ru.profitsw2000.youngmath.R
import youngmath.navigator.Navigator

class NavigatorImpl(
    private val navController: NavController
) : Navigator {
    override fun navigateToMultiplicationScreen() {
        navController.navigate(R.id.multiplicationFragment)
    }

    override fun navigateToMultiplicationTest() {
        navController.navigate(R.id.testMultiplicationFragment)
    }

    override fun navigateToMultiplicationTestResult() {
        navController.navigate(R.id.testResultFragment)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

}