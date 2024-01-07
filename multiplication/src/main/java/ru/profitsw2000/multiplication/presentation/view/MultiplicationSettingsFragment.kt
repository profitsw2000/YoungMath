package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.data.model.MultiplicationTestSettingsModel
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentMultiplicationSettingsBinding
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel
import youngmath.navigator.Navigator

class MultiplicationSettingsFragment : Fragment() {

    private var _binding: FragmentMultiplicationSettingsBinding? = null
    private val binding get() = _binding!!
    private val multiplicationViewModel: MultiplicationViewModel by viewModel()
    private val navigator: Navigator by inject()
    private lateinit var multiplicationTestSettingsModel: MultiplicationTestSettingsModel

    //Adapters
    private val tasksNumberAdapter by lazy {
        ArrayAdapter.createFromResource(
            requireContext(),
            ru.profitsw2000.core.R.array.examples_number_array,
            R.layout.drop_down_item
        )
    }

    private val taskTimeAdapter by lazy {
        ArrayAdapter.createFromResource(
            requireContext(),
            ru.profitsw2000.core.R.array.task_time_array,
            R.layout.drop_down_item
        )
    }

    private val fiveAssessmentErrorsNumberAdapter by lazy {
        ArrayAdapter<Int>(requireContext(), R.layout.drop_down_item)
    }

    private val fourAssessmentErrorsNumberAdapter by lazy {
        ArrayAdapter<Int>(requireContext(), R.layout.drop_down_item)
    }

    private val threeAssessmentErrorsNumberAdapter by lazy {
        ArrayAdapter<Int>(requireContext(), R.layout.drop_down_item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
        multiplicationTestSettingsModel = multiplicationViewModel.getMultiplicationSettings()
        populateSettingsForms()
        initViews()
    }

    private fun populateSettingsForms() {
        populateTasksNumberForm()
        populateTaskTimeForm()
        with(multiplicationTestSettingsModel) {
            populateFiveAssessmentErrorsNumberForm(fiveAssessmentErrorsNumber.toString(), tasksNumber)
            populateFourAssessmentErrorsNumberForm(fourAssessmentErrorsNumber.toString(), tasksNumber)
            populateThreeAssessmentErrorsNumberForm(threeAssessmentErrorsNumber.toString(), tasksNumber)
        }
        setTestDifficulty()
    }

    private fun initViews() {
        initTasksNumberForm()
        initFiveAssessmentErrorNumberForm()
        initFourAssessmentErrorNumberForm()
        initThreeAssessmentErrorNumberForm()
        initButton()
    }

    private fun populateTasksNumberForm() {
        multiplicationTestSettingsModel.let {
            binding.examplesNumberPickerAutoCompleteTextView.setText(it.tasksNumber.toString(),false)
        }
        binding.examplesNumberPickerAutoCompleteTextView.setAdapter(tasksNumberAdapter)
    }

    private fun populateTaskTimeForm() {
        multiplicationTestSettingsModel.let {
            binding.taskTimePickerAutoCompleteTextView.setText("%.0f".format(it.taskTime),false)
        }
        binding.taskTimePickerAutoCompleteTextView.setAdapter(taskTimeAdapter)
    }

    private fun populateFiveAssessmentErrorsNumberForm(tasksNumber: Int) {
        fiveAssessmentErrorsNumberAdapter.clear()
        fiveAssessmentErrorsNumberAdapter.addAll(
            IntRange(0, (tasksNumber - 3)).step(1).toList()
        )
        binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.setAdapter(fiveAssessmentErrorsNumberAdapter)
    }

    private fun populateFiveAssessmentErrorsNumberForm(viewText: String, tasksNumber: Int) {
        binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.setText(viewText, false)
        fiveAssessmentErrorsNumberAdapter.clear()
        fiveAssessmentErrorsNumberAdapter.addAll(
            IntRange(0, (tasksNumber - 3)).step(1).toList()
        )
        binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.setAdapter(fiveAssessmentErrorsNumberAdapter)
    }

    private fun populateFourAssessmentErrorsNumberForm(tasksNumber: Int) {
        fourAssessmentErrorsNumberAdapter.clear()
        fourAssessmentErrorsNumberAdapter.addAll(
            IntRange(1, (tasksNumber - 2)).step(1).toList()
        )
        binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setAdapter(fourAssessmentErrorsNumberAdapter)
    }

    private fun populateFourAssessmentErrorsNumberForm(viewText: String, tasksNumber: Int) {
        binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setText(viewText, false)
        fourAssessmentErrorsNumberAdapter.clear()
        fourAssessmentErrorsNumberAdapter.addAll(
            IntRange(1, (tasksNumber - 2)).step(1).toList()
        )
        binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setAdapter(fourAssessmentErrorsNumberAdapter)
    }

    private fun populateThreeAssessmentErrorsNumberForm(tasksNumber: Int) {
        threeAssessmentErrorsNumberAdapter.clear()
        threeAssessmentErrorsNumberAdapter.addAll(
            IntRange(2, (tasksNumber - 1)).step(1).toList()
        )
        binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.setAdapter(threeAssessmentErrorsNumberAdapter)
    }

    private fun populateThreeAssessmentErrorsNumberForm(viewText: String, tasksNumber: Int) {
        binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.setText(viewText, false)
        threeAssessmentErrorsNumberAdapter.clear()
        threeAssessmentErrorsNumberAdapter.addAll(
            IntRange(2, (tasksNumber - 1)).step(1).toList()
        )
        binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.setAdapter(threeAssessmentErrorsNumberAdapter)
    }

    private fun setTestDifficulty() {
        multiplicationTestSettingsModel.let {
            binding.highDifficultyTestSwitch.isChecked = it.isHighDifficulty
        }
    }
    
    private fun initTasksNumberForm() {
        binding.examplesNumberPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val tasksNumber: Int = adapterView.adapter.getItem(item).toString().toInt()

            if (binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt() >= tasksNumber) {
                populateFiveAssessmentErrorsNumberForm(0.toString(), tasksNumber)
                populateFourAssessmentErrorsNumberForm(1.toString(), tasksNumber)
                populateThreeAssessmentErrorsNumberForm(2.toString(), tasksNumber)
            } else {
                populateFiveAssessmentErrorsNumberForm(tasksNumber)
                populateFourAssessmentErrorsNumberForm(tasksNumber)
                populateThreeAssessmentErrorsNumberForm(tasksNumber)
            }
        }
    }

    private fun initFiveAssessmentErrorNumberForm() {
        binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val fiveAssessmentErrorNumber: Int = adapterView.adapter.getItem(item).toString().toInt()
            val fourAssessmentErrorNumber: Int = binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt()
            val threeAssessmentErrorNumber: Int = binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt()

            when {
                fourAssessmentErrorNumber in (fiveAssessmentErrorNumber + 1) until threeAssessmentErrorNumber -> {}
                fourAssessmentErrorNumber <= fiveAssessmentErrorNumber && threeAssessmentErrorNumber <= fiveAssessmentErrorNumber + 1 -> {
                    binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setText((fiveAssessmentErrorNumber + 1).toString(), false)
                    binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.setText((fiveAssessmentErrorNumber + 2).toString(), false)
                }
                fourAssessmentErrorNumber <= fiveAssessmentErrorNumber && threeAssessmentErrorNumber > fiveAssessmentErrorNumber + 1 -> {
                    binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setText((fiveAssessmentErrorNumber + 1).toString(), false)
                }
            }
        }
    }

    private fun initFourAssessmentErrorNumberForm() {
        binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val fiveAssessmentErrorNumber: Int = binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt()
            val fourAssessmentErrorNumber: Int = adapterView.adapter.getItem(item).toString().toInt()
            val threeAssessmentErrorNumber: Int = binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt()

            when {
                fourAssessmentErrorNumber in (fiveAssessmentErrorNumber + 1) until threeAssessmentErrorNumber -> {}
                fourAssessmentErrorNumber <= fiveAssessmentErrorNumber && threeAssessmentErrorNumber > fiveAssessmentErrorNumber + 1 -> {
                    binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.setText((fourAssessmentErrorNumber - 1).toString(), false)
                }
                fourAssessmentErrorNumber > fiveAssessmentErrorNumber && fourAssessmentErrorNumber >= threeAssessmentErrorNumber -> {
                    binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.setText((fourAssessmentErrorNumber + 1).toString(), false)
                }
            }
        }
    }

    private fun initThreeAssessmentErrorNumberForm() {
        binding.threeAssessmentErrorNumberPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val fiveAssessmentErrorNumber: Int = binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt()
            val fourAssessmentErrorNumber: Int = binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt()
            val threeAssessmentErrorNumber: Int = adapterView.adapter.getItem(item).toString().toInt()

            when {
                fourAssessmentErrorNumber in (fiveAssessmentErrorNumber + 1) until threeAssessmentErrorNumber -> {}
                threeAssessmentErrorNumber <= fourAssessmentErrorNumber && threeAssessmentErrorNumber <= (fiveAssessmentErrorNumber + 1) -> {
                    binding.fiveAssessmentErrorNumberPickerAutoCompleteTextView.setText((threeAssessmentErrorNumber - 2).toString(), false)
                    binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setText((threeAssessmentErrorNumber - 1).toString(), false)
                }
                threeAssessmentErrorNumber <= fourAssessmentErrorNumber && threeAssessmentErrorNumber > (fiveAssessmentErrorNumber + 1) -> {
                    binding.fourAssessmentErrorNumberPickerAutoCompleteTextView.setText((threeAssessmentErrorNumber - 1).toString(), false)
                }
            }
        }
    }

    private fun initButton() = with(binding) {
        setMultiplicationSettingsButton.setOnClickListener {
            showApplySettingsDialog()
        }
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(ru.profitsw2000.core.R.string.settings_screen_title_text))
            .setMessage(getString(ru.profitsw2000.core.R.string.exit_settings_screen_dialog_message_text))
            .setPositiveButton(getString(ru.profitsw2000.core.R.string.settings_dialog_positive_button_text)) { _, _ ->
                navigator.navigateUp()
            }
            .setNegativeButton(getString(ru.profitsw2000.core.R.string.settings_dialog_negative_button_text)) { dialog , _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showApplySettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(ru.profitsw2000.core.R.string.settings_screen_title_text))
            .setMessage(getString(R.string.apply_settings_dialog_message_text))
            .setPositiveButton(getString(ru.profitsw2000.core.R.string.settings_dialog_positive_button_text)) { _, _ ->
                applySettingsAndExit()
            }
            .setNegativeButton(getString(ru.profitsw2000.core.R.string.settings_dialog_negative_button_text)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun applySettingsAndExit() = with(binding) {
        val newMultiplicationTestSettingsModel = MultiplicationTestSettingsModel(
            taskTime = taskTimePickerAutoCompleteTextView.text.toString().toFloat(),
            tasksNumber = examplesNumberPickerAutoCompleteTextView.text.toString().toInt(),
            fiveAssessmentErrorsNumber = fiveAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt(),
            fourAssessmentErrorsNumber = fourAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt(),
            threeAssessmentErrorsNumber = threeAssessmentErrorNumberPickerAutoCompleteTextView.text.toString().toInt(),
            isHighDifficulty = highDifficultyTestSwitch.isChecked
        )
        multiplicationViewModel.writeMultiplicationSettingsToSharedPreferences(newMultiplicationTestSettingsModel)
        multiplicationViewModel.updateMultiplicationTestSettings(newMultiplicationTestSettingsModel)
        navigator.navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}