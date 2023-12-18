package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.data.model.state.MultiplicationHistoryState
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentMultiplicationHistoryDetailsBinding
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar


class MultiplicationHistoryDetailsFragment : Fragment() {

    private var _binding: FragmentMultiplicationHistoryDetailsBinding? = null
    private val binding get() = _binding!!
    private val multiplicationViewModel: MultiplicationViewModel by viewModel()
    private val testId: Int? by lazy { arguments?.getInt(TEST_ID, 0) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMultiplicationHistoryDetailsBinding.bind(inflater.inflate(R.layout.fragment_multiplication_history_details, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        testId?.let {
            multiplicationViewModel.getMultiplicationTestResultsListById(it)
        }
    }

    private fun initViews() {

    }

    private fun observeData() {
        val observer = Observer<MultiplicationHistoryState>() { renderData(it) }
        multiplicationViewModel.multiplicationHistoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(multiplicationHistoryState: MultiplicationHistoryState) {
        when(multiplicationHistoryState) {
            is MultiplicationHistoryState.Error -> handleError(multiplicationHistoryState.message)
            MultiplicationHistoryState.Loading -> setProgressBarVisible(true)
            is MultiplicationHistoryState.Success -> handleSuccess(multiplicationHistoryState.multiplicationHistoryModelList[0])
        }
    }

    private fun handleError(message: String) = with(binding) {
        setProgressBarVisible(false)
        Snackbar.make(this.multiplicationHistoryDetailsFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(ru.profitsw2000.core.R.string.snackbar_reload_text)) {
                multiplicationViewModel.getMultiplicationTestResultsList()
            }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            mainGroup.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            mainGroup.visibility = View.VISIBLE
        }
    }

    private fun handleSuccess(multiplicationHistoryModel: MultiplicationHistoryModel) {
        setProgressBarVisible(false)
        populateViews(multiplicationHistoryModel)
        addExamples(multiplicationHistoryModel)
    }

    private fun populateViews(multiplicationHistoryModel: MultiplicationHistoryModel) = with(binding) {
        rightAnswersNumberTextView.text = getString(R.string.right_answers_number_text,
            multiplicationHistoryModel.rightAnswers.toString())
        wrongAnswersNumberTextView.text = getString(R.string.wrong_answers_number_text,
            multiplicationHistoryModel.wrongAnswers.toString())
        resultAssessmentTextView.text = getString(R.string.test_assessment_text,
            multiplicationHistoryModel.assessment.toString())
        totalTestTimeTextView.text = getString(R.string.total_test_time_text,
            "%.2f".format(multiplicationHistoryModel.testTime))
        testDateTextView.text = getString(R.string.test_execution_date_text,
            SimpleDateFormat("dd.MM.yyyy HH:mm").format(multiplicationHistoryModel.testDate))
        if (multiplicationHistoryModel.isInterrupted) testWasSkippedTextView.visibility = View.VISIBLE
        else testWasSkippedTextView.visibility = View.GONE
    }

    private fun addExamples(multiplicationHistoryModel: MultiplicationHistoryModel) = with(binding) {
        val firstMultiplicatorList = multiplicationHistoryModel.firstMultiplicatorList
        val secondMultiplicatorList = multiplicationHistoryModel.secondMultiplicatorList
        val userMultiplicationResultsList = multiplicationHistoryModel.userMultiplicationResults
        val multiplicationResultsList = multiplicationHistoryModel.multiplicationResults
        val constraintLayout = examplesLinearLayout

        firstMultiplicatorList.forEachIndexed { index, element ->
            val exampleTextView = TextView(context)

            exampleTextView.text = "${index + 1})   $element * ${secondMultiplicatorList[index]} = ${userMultiplicationResultsList[index]}"
            exampleTextView.setTextColor(getTextColorFromResult(
                userMultiplicationResultsList[index],
                multiplicationResultsList[index])
            )
            exampleTextView.textSize = resources.getDimension(ru.profitsw2000.core.R.dimen.result_small_sub_title_text_size) / resources.displayMetrics.density
            exampleTextView.setPadding(0, 0, 0,
                resources.getDimension(ru.profitsw2000.core.R.dimen.extra_small_space_between_views)
                    .toInt()
            )
            constraintLayout.addView(exampleTextView)
        }
    }

    private fun getTextColorFromResult(userMultiplicationResult: Int, multiplicationResult: Int): Int {
        return if (userMultiplicationResult == multiplicationResult) ContextCompat.getColor(requireContext(), ru.profitsw2000.core.R.color.green)
        else ContextCompat.getColor(requireContext(), ru.profitsw2000.core.R.color.red)
    }

    companion object{
        val TEST_ID = "test_id"
    }
}