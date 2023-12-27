package com.example.stepcount.ui.profile.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stepcount.data.model.HistoryData
import com.example.stepcount.databinding.ItemHistoryBinding
import com.example.stepcount.utils.TrackingUtility
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    var onClickItem: ((HistoryData) -> Unit)? = null
    inner class ViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.date
        val image = binding.imgHistory
        val sumKm = binding.tvSumKm
        val time = binding.tvTimeHistory
        val sumKcal = binding.tvSumKcal
        val sumStep = binding.tvSumStep
    }

    private val diffCallback = object : DiffUtil.ItemCallback<HistoryData>() {
        override fun areItemsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<HistoryData>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyData = differ.currentList[position]
        holder.itemView.apply {
            val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            holder.date.text = formatDate.format(historyData.date)
            Glide.with(this).load(historyData.img).into(holder.image)
            holder.time.text = TrackingUtility.getFormattedStopWatchTime(historyData.time)
            holder.sumKm.text = historyData.distance.toString()
            holder.sumKcal.text = formatConvertString(historyData.calories.toInt(), "Kcal")
            holder.sumStep.text = formatConvertString(historyData.steps.toInt(), "Steps")
            setOnClickListener {
                onClickItem?.invoke(historyData)
            }
        }
    }

    private fun formatConvertString(values: Int, str: String) = String.format("$values $str")
}