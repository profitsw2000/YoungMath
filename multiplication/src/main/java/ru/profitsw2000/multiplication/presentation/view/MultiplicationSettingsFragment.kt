package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentMultiplicationSettingsBinding

class MultiplicationSettingsFragment : Fragment() {

    private var _binding: FragmentMultiplicationSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMultiplicationSettingsBinding.bind(inflater.inflate(R.layout.fragment_multiplication_settings, container, false))
        return binding.root
    }
}