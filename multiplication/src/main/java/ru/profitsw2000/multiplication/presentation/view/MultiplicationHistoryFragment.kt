package ru.profitsw2000.multiplication.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.core.utils.OnMathCategoryClickListener
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.data.model.state.MultiplicationHistoryState
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.FragmentMultiplicationBinding
import ru.profitsw2000.multiplication.databinding.FragmentMultiplicationHistoryBinding
import ru.profitsw2000.multiplication.presentation.utils.OnHistoryListEventsListener
import ru.profitsw2000.multiplication.presentation.view.MultiplicationHistoryDetailsFragment.Companion.TEST_ID
import ru.profitsw2000.multiplication.presentation.view.adapter.MultiplicationHistoryAdapter
import ru.profitsw2000.multiplication.presentation.viewmodel.MultiplicationViewModel
import youngmath.navigator.Navigator


class MultiplicationHistoryFragment : Fragment() {

    private var _binding: FragmentMultiplicationHistoryBinding? = null
    private val binding get() = _binding!!
    private val multiplicationViewModel: MultiplicationViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = MultiplicationHistoryAdapter(object : OnHistoryListEventsListener{

        override fun onItemClick(multiplicationHistoryModel: MultiplicationHistoryModel) {
            val bundle = Bundle().apply {
                putParcelable(TEST_ID, multiplicationHistoryModel)
            }
            this@MultiplicationHistoryFragment.arguments = bundle
            navigator.navigateToMultiplicationHistoryDetails(bundle)
        }

        override fun onPositionChanged(position: Int) {
            multiplicationViewModel.updateCurrentMultiplicationHistoryListPosition(position)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMultiplicationHistoryBinding.bind(inflater.inflate(R.layout.fragment_multiplication_history, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {
        multiplicationHistoryListRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val observer = Observer<MultiplicationHistoryState>() { renderData(it) }
        multiplicationViewModel.multiplicationHistoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(multiplicationHistoryState: MultiplicationHistoryState) {
        when(multiplicationHistoryState) {
            is MultiplicationHistoryState.Error -> handleError(multiplicationHistoryState.message)
            MultiplicationHistoryState.Loading -> setProgressBarVisible(true)
            is MultiplicationHistoryState.Success -> handleSuccess(multiplicationHistoryState.multiplicationHistoryModelList)
        }
    }

    private fun handleError(message: String) = with(binding) {
        Snackbar.make(this.historyFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(ru.profitsw2000.core.R.string.snackbar_reload_text)) {
                multiplicationViewModel.getMultiplicationTestResultsList()
            }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            multiplicationHistoryListRecyclerView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            multiplicationHistoryListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun handleSuccess(multiplicationHistoryModelList: List<MultiplicationHistoryModel>) {
        setProgressBarVisible(false)
        adapter.setData(multiplicationHistoryModelList)
    }

}