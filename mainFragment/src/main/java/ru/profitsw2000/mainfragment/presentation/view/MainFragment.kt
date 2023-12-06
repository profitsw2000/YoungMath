package ru.profitsw2000.mainfragment.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.mainfragment.R
import diarynote.mainfragment.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.core.utils.OnMathCategoryClickListener
import ru.profitsw2000.core.view.CoreFragment
import ru.profitsw2000.core.viewmodel.CoreViewModel
import ru.profitsw2000.data.model.MathCategoryModel
import ru.profitsw2000.mainfragment.presentation.view.adapter.MathCategoriesAdapter
import ru.profitsw2000.mainfragment.presentation.viewmodel.MainViewModel

class MainFragment : CoreFragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override val viewModel: MainViewModel by viewModel()
    private val adapter = MathCategoriesAdapter(object : OnMathCategoryClickListener{
        override fun onItemClick(categoryId: Int) {

        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        viewModel.getCategoryList()
    }

    private fun initViews() = with(binding) {
        mathCategoriesListRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val observer = Observer<List<MathCategoryModel>> { renderData(it) }
        viewModel.categoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(mathCategoryModelList: List<MathCategoryModel>) {
        adapter.setData(mathCategoryModelList)
    }
}