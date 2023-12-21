package ru.profitsw2000.multiplication.presentation.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.core.utils.OnMathCategoryClickListener
import ru.profitsw2000.data.model.MathCategoryModel
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.HistoryListItemViewBinding
import java.text.SimpleDateFormat

class MultiplicationHistoryAdapter(
    private val onMathCategoryClickListener: OnMathCategoryClickListener
) : RecyclerView.Adapter<MultiplicationHistoryAdapter.ViewHolder>() {

    private var data: List<MultiplicationHistoryModel> = arrayListOf()

    fun setData(data: List<MultiplicationHistoryModel>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val multiplicationHistoryViewHolder = ViewHolder(binding)

        binding.historyListItemViewRootLayout.setOnClickListener {
            onMathCategoryClickListener.onItemClick(data[multiplicationHistoryViewHolder.layoutPosition].id)
        }

        return multiplicationHistoryViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.cardView.context

        with(holder) {
            //Texts
            testAssessment.text = context.getString(R.string.test_assessment_text,
                data[position].assessment.toString())
            testDuration.text = context.getString(R.string.total_test_time_text,
                "%.2f".format(data[position].testTime))
            testDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(data[position].testDate)
            testId.text = context.getString(R.string.test_id_number_text,
                data[position].id.toString())

            //Colors
            testAssessment.setTextColor(ContextCompat.getColor(context,
                getViewTextColorFromAssessment(context, data[position].assessment)))
            cardView.setCardBackgroundColor(ContextCompat.getColor(context,
                getCardBackgroundColorFromAssessment(context, data[position].assessment)))
            cardView.strokeColor = ContextCompat.getColor(context,
                getViewTextColorFromAssessment(context, data[position].assessment))
        }
        Log.d("VVV", "onBindViewHolder, position: $position")
    }

    private fun getViewTextColorFromAssessment(context: Context, assessment: Int): Int {
        return when(assessment) {
            5 -> ru.profitsw2000.core.R.color.green
            4 -> ru.profitsw2000.core.R.color.orange
            3 -> ru.profitsw2000.core.R.color.deep_blue
            2 -> ru.profitsw2000.core.R.color.red
            else -> ru.profitsw2000.core.R.color.red
        }
    }

    private fun getCardBackgroundColorFromAssessment(context: Context, assessment: Int): Int {
        return when(assessment) {
            5 -> ru.profitsw2000.core.R.color.transparent_green
            4 -> ru.profitsw2000.core.R.color.transparent_orange
            3 -> ru.profitsw2000.core.R.color.transparent_blue
            2 -> ru.profitsw2000.core.R.color.transparent_red
            else -> ru.profitsw2000.core.R.color.transparent_red
        }
    }

    inner class ViewHolder(binding: HistoryListItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        val cardView = binding.historyListItemViewRootLayout
        val testAssessment = binding.testAssessmentTextView
        val testDuration = binding.totalTestTimeTextView
        val testDate = binding.testDateTextView
        val testId = binding.testIdTextView
    }
}