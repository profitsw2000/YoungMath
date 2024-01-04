package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentMultiplicationSettingsBinding

class MultiplicationSettingsFragment : Fragment() {

    private var _binding: FragmentMultiplicationSettingsBinding? = null
    private val binding get() = _binding!!
    private val examplesNumberAdapter by lazy {
        ArrayAdapter<Int>(requireContext(), R.layout.drop_down_item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMultiplicationSettingsBinding.bind(inflater.inflate(R.layout.fragment_multiplication_settings, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.examplesNumberPickerAutoCompleteTextView.setText("20")
        examplesNumberAdapter.addAll(10,20,30,40,50)
        binding.examplesNumberPickerAutoCompleteTextView.setAdapter(examplesNumberAdapter)
    }
}