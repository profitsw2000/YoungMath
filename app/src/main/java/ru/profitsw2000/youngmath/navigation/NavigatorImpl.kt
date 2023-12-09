package ru.profitsw2000.youngmath.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.profitsw2000.youngmath.R
import youngmath.navigator.Navigator

class NavigatorImpl(
    private val navController: NavController
) : Navigator {
    override fun navigateToMultiplicationScreen() {
        navController.navigate(R.id.multiplication_nav_graph)
    }

    override fun navigateToMultiplicationTest() {
        navController.navigate(R.id.testMultiplicationFragment)
    }

    override fun navigateToMultiplicationTestResult() {
        val bundle = Bundle()
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.testMultiplicationFragment, true).build()
        navController.navigate(R.id.testResultFragment, bundle, navOptions)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

}