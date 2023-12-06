package ru.profitsw2000.mainfragment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.mainfragment.databinding.MathCategoryItemViewBinding
import ru.profitsw2000.core.utils.OnMathCategoryClickListener
import ru.profitsw2000.data.model.MathCategoryModel

class MathCategoriesAdapter(
    private val onMathCategoryClickListener: OnMathCategoryClickListener
) : RecyclerView.Adapter<MathCategoriesAdapter.ViewHolder>() {

    private var data: List<MathCategoryModel> = arrayListOf()

    fun setData(data: List<MathCategoryModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MathCategoriesAdapter.ViewHolder {
        val binding = MathCategoryItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mathCategoryViewHolder = ViewHolder(binding)

        binding.mathCategoryItemRootLayout.setOnClickListener {
            onMathCategoryClickListener.onItemClick(data[mathCategoryViewHolder.adapterPosition].id)
        }

        return mathCategoryViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mathCategoryModel = data[position]

        holder.mathCategoryTitle.text = mathCategoryModel.name
    }


    inner class ViewHolder(binding: MathCategoryItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val mathCategoryTitle = binding.mathCategoryItemTitleTextView
    }
}