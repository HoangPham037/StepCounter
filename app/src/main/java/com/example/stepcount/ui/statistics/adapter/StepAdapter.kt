package com.example.stepcount.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.databinding.ProgressItemBinding
import com.example.stepcount.ui.statistics.ProgressBarAnimation
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StepAdapter : RecyclerView.Adapter<StepAdapter.ViewHolder>() {
    private var list = ArrayList<StepsData>()

    inner class ViewHolder(private val binding: ProgressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stepsData: StepsData) = with(binding) {
            if (stepsData.state == true) {
                imgStar.visibility = View.VISIBLE
                tvLevel.visibility = View.VISIBLE

                val level: Int = when {
                    stepsData.steps!! >= 8000 -> 1
                    stepsData.steps!! >= 7000 -> 2
                    stepsData.steps!! >= 6000 -> 3
                    else -> 4
                }
                when (level) {
                    1 -> tvLevel.text = level.toString()
                    2 -> tvLevel.text = level.toString()
                    3 -> tvLevel.text = level.toString()
                    4 -> tvLevel.text = level.toString()
                }
            }

            progress.apply {
                max = 6000
                progress = stepsData.steps!!.toInt()

            }
            val animation = ProgressBarAnimation(progress, 0f, progress.progress.toFloat())
            animation.duration = 1000
            progress.startAnimation(animation)

            tvSteps.text = stepsData.steps!!.toInt().toString()
            tvNameDay.text = stepsData.nameDay

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProgressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(list: ArrayList<StepsData>) {
        this.list = list
    }
}