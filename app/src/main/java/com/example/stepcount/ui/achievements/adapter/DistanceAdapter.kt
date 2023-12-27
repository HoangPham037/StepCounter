package com.example.stepcount.ui.achievements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.R
import com.example.stepcount.databinding.ItemDistanceStepsBinding
import com.example.stepcount.ui.achievements.model.DataCommon

class DistanceAdapter(
    val context: Context,
    private val daily: List<DataCommon>,
    private val distance: Float,
    private val mode: Int
) : RecyclerView.Adapter<DistanceAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemDistanceStepsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: DataCommon) = with(binding) {
            if (distance >= daily.unit) {
                viewBgr.setBackgroundResource(R.drawable.custom_bgr_blue_item)
                imgDistance.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
            val layoutManager = binding.layoutContainer.layoutParams
            layoutManager.width = mode
            binding.layoutContainer.layoutParams = layoutManager
            tvDistances.text= daily.unit.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDistanceStepsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = daily.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(daily[position])
}

