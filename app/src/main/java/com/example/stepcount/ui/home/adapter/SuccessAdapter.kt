package com.example.stepcount.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.databinding.AchievementsItemBinding

class SuccessAdapter : RecyclerView.Adapter<SuccessAdapter.ViewHolder>() {
    private var list = ArrayList<StepsData>()

    inner class ViewHolder(private val binding: AchievementsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stepsData: StepsData) = with(binding) {
            if (stepsData.state == true) {
                achievementIcon.visibility = View.VISIBLE
            }
            achievementText.text = stepsData.nameDay
            achievementProgress.max = 6000
            achievementProgress.progress = stepsData.steps!!.toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuccessAdapter.ViewHolder {
        return ViewHolder(
            AchievementsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SuccessAdapter.ViewHolder, position: Int) =holder.bind(list[position])

    override fun getItemCount(): Int=list.size

    fun setData(steps: ArrayList<StepsData>){
        this.list = steps
    }

}