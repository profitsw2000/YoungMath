package ru.profitsw2000.youngmath.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.profitsw2000.youngmath.R
import ru.profitsw2000.youngmath.databinding.ActivityMainBinding
import ru.profitsw2000.youngmath.navigation.NavigatorImpl
import youngmath.navigator.Navigator

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        ) as NavHostFragment

        navController = navHostFragment.navController
        loadKoinModules(module { single<Navigator> { NavigatorImpl(navController) } })
    }
}