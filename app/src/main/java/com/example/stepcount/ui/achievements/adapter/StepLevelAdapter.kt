package com.example.stepcount.ui.achievements.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.R
import com.example.stepcount.databinding.ItemStepsLevelBinding
import com.example.stepcount.ui.achievements.model.DataCommon
import com.example.stepcount.ui.achievements.model.StepsLevelData
import kotlinx.android.synthetic.main.item_daily_steps.view.view2

class StepLevelAdapter(
    private val myList: List<StepsLevelData>,
    private val context: Context,
    private val steps: Float
) :
    RecyclerView.Adapter<StepLevelAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemStepsLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stepsLevelData: StepsLevelData) = with(binding) {
            if (steps >= stepsLevelData.sumSteps) {
                viewBgr.setBackgroundResource(R.drawable.custom_bgr_blue_item)
                tvNumStep.setTextColor(ContextCompat.getColor(context, R.color.color_text_title))
                imgLock.visibility = View.GONE
                dropDowLevel.setColorFilter(ContextCompat.getColor(context, R.color.bg_blue))

            }
            tvNumStep.text = String.format("%d%s",stepsLevelData.sumStepLevel, "K" )
            tvLevel.text = stepsLevelData.level
            if (stepsLevelData.sumStepLevel == 0) {
                tvNumStep.text = "start"
                tvStepsText.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStepsLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = myList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(myList[position])
}