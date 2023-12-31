package ru.profitsw2000.multiplication.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.data.model.MultiplicationHistoryModel
import ru.profitsw2000.multiplication.R
import ru.profitsw2000.multiplication.databinding.HistoryListItemViewBinding
import ru.profitsw2000.multiplication.presentation.utils.OnHistoryListEventsListener
import java.text.SimpleDateFormat

class MultiplicationHistoryAdapter(
    private val onHistoryListEventsListener: OnHistoryListEventsListener
) : RecyclerView.Adapter<MultiplicationHistoryAdapter.ViewHolder>() {

    var data: List<MultiplicationHistoryModel> = arrayListOf()
        private set

    fun setData(data: List<MultiplicationHistoryModel>){
        this.data = data
        //notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val multiplicationHistoryViewHolder = ViewHolder(binding)

        binding.historyListItemViewRootLayout.setOnClickListener {
            onHistoryListEventsListener.onItemClick(data[multiplicationHistoryViewHolder.layoutPosition])
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
            testAssessment.text = if (data[position].assessment != 1) {
                context.getString(R.string.test_assessment_text,
                    data[position].assessment.toString())
            } else {
                context.getString(R.string.no_test_assessment_pre_text) + context.getString(ru.profitsw2000.core.R.string.no_assessment_brief_text)
            }
            testDuration.text = context.getString(R.string.total_test_time_text,
                "%.2f".format(data[position].testTime))
            testDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(data[position].testDate)
            testId.text = context.getString(R.string.test_id_number_text,
                data[position].id.toString())
            if (data[position].isHighDifficulty) isHighDifficultySign.visibility = View.VISIBLE
            else isHighDifficultySign.visibility = View.GONE

            //Colors
            testAssessment.setTextColor(ContextCompat.getColor(context,
                getViewTextColorFromAssessment(context, data[position].assessment)))
            cardView.setCardBackgroundColor(ContextCompat.getColor(context,
                getCardBackgroundColorFromAssessment(context, data[position].assessment)))
            cardView.strokeColor = ContextCompat.getColor(context,
                getViewTextColorFromAssessment(context, data[position].assessment))
        }
        //Log.d("VVV", "onBindViewHolder, position: $position")
        onHistoryListEventsListener.onPositionChanged(position)
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
        val isHighDifficultySign = binding.highDifficultyImageView
    }
}