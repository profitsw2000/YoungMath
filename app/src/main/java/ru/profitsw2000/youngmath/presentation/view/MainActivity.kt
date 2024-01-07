package ru.profitsw2000.youngmath.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel
import ru.profitsw2000.youngmath.R
import ru.profitsw2000.youngmath.databinding.ActivityMainBinding
import ru.profitsw2000.youngmath.navigation.NavigatorImpl
import youngmath.navigator.Navigator

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val multiplicationViewModel: MultiplicationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        setActionBar()
        setMultiplicationSettings()
        multiplicationViewModel.checkForInterruptedTest()
    }

    private fun initNavigation() {

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        ) as NavHostFragment

        navController = navHostFragment.navController
        loadKoinModules(module { single<Navigator> { NavigatorImpl(navController) } })

    }

    private fun setActionBar() {

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id) {
                R.id.multiplicationFragment -> actionBar?.show()
                else -> actionBar?.hide()
            }
        }
    }

    private fun setMultiplicationSettings() {
        val multiplicationTestSettingsModel = multiplicationViewModel.getMultiplicationSettings()

        multiplicationViewModel.updateMultiplicationTestSettings(multiplicationTestSettingsModel)
    }
}