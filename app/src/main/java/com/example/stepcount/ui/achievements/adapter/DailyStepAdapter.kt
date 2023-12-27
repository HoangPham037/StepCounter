package com.example.stepcount.ui.achievements.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.R
import com.example.stepcount.databinding.ItemDailyStepsBinding
import com.example.stepcount.ui.achievements.model.DataCommon

class DailyStepAdapter(
    val context: Context,
    private val daily: List<DataCommon>,
    private val steps: Float,
    private val mode: Int
) : RecyclerView.Adapter<DailyStepAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemDailyStepsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: DataCommon) = with(binding) {
            if (steps > 0 && steps/1000 >= daily.unit) {
                viewBgr.setBackgroundResource(R.drawable.custom_bgr_blue_item)
                imgItemDaily.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
            val layoutManager = binding.layoutContainer.layoutParams
            layoutManager.width = mode
            binding.layoutContainer.layoutParams = layoutManager
            tvDailyStep.text = daily.unit.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDailyStepsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = daily.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(daily[position])
}

