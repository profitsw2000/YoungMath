package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentTestResultBinding
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel
import youngmath.navigator.Navigator

class TestResultFragment : Fragment() {

    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!
    private val multiplicationViewModel: MultiplicationViewModel by viewModel()
    private val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTestResultBinding.bind(inflater.inflate(R.layout.fragment_test_result, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {

    }

    private fun observeData() {
        multiplicationViewModel.multiplicationTestResultsLiveData.observe(viewLifecycleOwner) {
            showResult(it)
        }
    }

    private fun showResult(results: Array<Boolean>) = with(binding) {
        rightAnswersNumberTextView.text = getString(R.string.right_answers_number_text,
            getBoolElementsInArrayNumber(true, results).toString())
        wrongAnswersNumberTextView.text = getString(R.string.wrong_answers_number_text,
            getBoolElementsInArrayNumber(false, results).toString())
        resultAssessmentTextView.text = getAssessment(results).toString()
        context?.let {
            resultAssessmentTextView.setTextColor(ContextCompat.getColor(it, getAssessmentTextColor(getAssessment(results))))
        }
    }

    private fun getBoolElementsInArrayNumber(element: Boolean, results: Array<Boolean>): Int {
        var number = 0

        results.forEach {
            if (it == element) number++
        }
        return number
    }

    private fun getAssessment(results: Array<Boolean>): Int {
        return when(getBoolElementsInArrayNumber(false, results)) {
            0 -> 5
            1 -> 4
            2 -> 3
            else -> 2
        }
    }

    private fun getAssessmentTextColor(assessment: Int): Int {
        return when(assessment) {
            5 -> ru.profitsw2000.core.R.color.green
            4 -> ru.profitsw2000.core.R.color.lime
            3 -> ru.profitsw2000.core.R.color.deep_blue
            2 -> ru.profitsw2000.core.R.color.red
            else -> ru.profitsw2000.core.R.color.red
        }
    }
}