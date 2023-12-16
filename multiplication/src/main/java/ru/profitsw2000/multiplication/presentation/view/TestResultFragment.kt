package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.data.model.state.MultiplicationHistoryState
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
        observeTestHistoryData()
        observeHistoryDbData()
    }

    private fun initViews() = with(binding) {

    }

    private fun observeTestHistoryData() {
        multiplicationViewModel.multiplicationHistoryResultsLiveData.observe(viewLifecycleOwner) {
            showResult(it)
            multiplicationViewModel.writeMultiplicationTestResult(it)
        }
    }

    private fun observeHistoryDbData() {
        val observer = Observer<MultiplicationHistoryState>() { renderData(it) }
        multiplicationViewModel.multiplicationHistoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun showResult(multiplicationHistoryModel: MultiplicationHistoryModel) = with(binding) {
        rightAnswersNumberTextView.text = getString(R.string.right_answers_number_text,
            multiplicationHistoryModel.rightAnswers.toString())
        wrongAnswersNumberTextView.text = getString(R.string.wrong_answers_number_text,
            multiplicationHistoryModel.wrongAnswers.toString())
        resultAssessmentTextView.text = multiplicationHistoryModel.assessment.toString()
        context?.let {
            resultAssessmentTextView.setTextColor(ContextCompat.getColor(it, getAssessmentTextColor(multiplicationHistoryModel.assessment)))
        }
        totalTestTimeTextView.text = getString(R.string.total_test_time_text,
            "%.2f".format(multiplicationHistoryModel.testTime))
    }

    private fun renderData(multiplicationHistoryState: MultiplicationHistoryState) {
        when(multiplicationHistoryState) {
            is MultiplicationHistoryState.Error -> handleError(multiplicationHistoryState.message)
            MultiplicationHistoryState.Loading -> setProgressBarVisible(true)
            is MultiplicationHistoryState.Success -> handleSuccess()
        }
    }

    private fun handleSuccess() {
        setProgressBarVisible(false)
        Toast.makeText(context,
            ru.profitsw2000.core.R.string.test_result_write_to_db_success,
            Toast.LENGTH_SHORT).show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun handleError(message: String) = with(binding) {
        setProgressBarVisible(false)
        Toast.makeText(context,
            //ru.profitsw2000.core.R.string.test_result_write_to_db_error,
            message,
            Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}