package ru.profitsw2000.multiplication.presentation.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentTestMultiplicationBinding
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel
import youngmath.navigator.Navigator

class TestMultiplicationFragment : Fragment() {

    private var _binding: FragmentTestMultiplicationBinding? = null
    private val binding get() = _binding!!
    private val multiplicationViewModel: MultiplicationViewModel by viewModel()
    private val navigator: Navigator by inject()
    private var taskNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTestMultiplicationBinding.bind(inflater.inflate(R.layout.fragment_test_multiplication, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {
        sendAnswerImageView.setOnClickListener {
            multiplicationResultEditText.text.toString().apply {
                if (this != "") multiplicationViewModel.nextTask(this.toInt())
            }
        }
        multiplicationResultEditText.showKeyboard()
    }

    private fun observeData() = with(binding) {
        multiplicationViewModel.multiplicationTestLiveData.observe(viewLifecycleOwner) {

            this.testNumberTextView.text = it.taskNumber.toString()
            this.testTimeMillisecondsTextView.text = formatTime(it.time)
            this.testTaskFirstMultiplicatorTextView.text = it.firstMultiplier.toString()
            this.testTaskSecondMultiplicatorTextView.text = it.secondMultiplier.toString()

            if (it.taskNumber != taskNumber) this.multiplicationResultEditText.text?.clear()
            taskNumber = it.taskNumber

            if (!it.isTestOn) navigator.navigateToMultiplicationTestResult()
        }
    }

    private fun formatTime(timeWithMillis: Float): String {
        return "%.2f".format(timeWithMillis)
    }

    fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

}