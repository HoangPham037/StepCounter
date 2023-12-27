package com.example.stepcount.ui.statistics

import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.stepcount.DateHelper
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.databinding.FragmentStatisticsBinding
import com.example.stepcount.ui.home.adapter.SuccessAdapter
import com.example.stepcount.ui.statistics.adapter.StepAdapter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.tabs.TabLayout
import java.text.NumberFormat
import java.util.*
import kotlinx.android.synthetic.main.fragment_statistics.view.circle_view_1

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(
    FragmentStatisticsBinding::inflate
) {
    private val statistics by lazy { StepAdapter() }
    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
     }
    override fun setupDataObserver() {
        super.setupDataObserver()
        getWeekStep()
        getMonthStep()
        getAllSteps()
    }

    private fun getWeekStep() {
        binding.rcWeekly.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.CENTER
        }
        appViewModel.step.observe(viewLifecycleOwner) { listStep ->
            if (!listStep.isNullOrEmpty()) {
                binding.circleView1.setBackgroundResource(R.drawable.circle_border_blue)
                binding.circleView2.setBackgroundResource(R.drawable.circle_border_pink)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    appViewModel.create(listStep as ArrayList<StepsData>).also { listSteps ->
                        statistics.setData(listSteps)
                        binding.rcWeekly.adapter = statistics
                    }
                }
                val sumStep = listStep.filter { it.steps!! > 0 }.sumOf { step ->
                    step.steps!!.toInt()
                }
                binding.tvTotalStepWeek.text = NumberFormat.getNumberInstance(Locale.US).format(sumStep)
                val averageWeek = sumStep / 7
                binding.tvAverageWeek.text = NumberFormat.getNumberInstance(Locale.US).format(averageWeek)
            } else {
                binding.circleView1.setBackgroundResource(R.drawable.circle_border_empty)
                binding.circleView2.setBackgroundResource(R.drawable.circle_border_empty)
                val calendar = Calendar.getInstance()
                val day = calendar[Calendar.DAY_OF_MONTH]
                val month = calendar[Calendar.MONTH] + 1
                val year = calendar[Calendar.YEAR]

                val listEmpty = arrayListOf(
                    StepsData(
                        0f,
                        DateHelper.name(day, month, year),
                        day,
                        month,
                        year,
                        0f,
                        0f,
                        0,
                        false
                    )
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    appViewModel.create(listEmpty).also {
                        statistics.setData(it)
                        binding.rcWeekly.adapter = statistics
                    }
                }
            }
        }

        appViewModel.fetchItemSteps()
    }

    private fun getMonthStep() {
        appViewModel.stepsMonth.observe(viewLifecycleOwner) { listStepMonth ->
            if (!listStepMonth.isNullOrEmpty()) {
                binding.circleView3.setBackgroundResource(R.drawable.circle_border_yellow)
                binding.circleView4.setBackgroundResource(R.drawable.circle_border_green)
                val sumStepInMonth = listStepMonth.map { it.steps }.reduce { pos1, pos2 -> pos1!! + pos2!! }
                val average = String.format("%.0f", sumStepInMonth!!/30).toInt()
                binding.tvTotalStepMonth.text = NumberFormat.getNumberInstance(Locale.US).format(sumStepInMonth)
                binding.tvAverageMonth.text = NumberFormat.getNumberInstance(Locale.US).format(average)
            } else {
                binding.circleView3.setBackgroundResource(R.drawable.circle_border_empty)
                binding.circleView4.setBackgroundResource(R.drawable.circle_border_empty)
            }

        }
        appViewModel.fetchItemStepsMonth()
    }

    private fun getAllSteps() {
        appViewModel.stepsAll.observe(viewLifecycleOwner) { listAllSteps ->
            if (!listAllSteps.isNullOrEmpty()) {
                val sumStepInLifeTime =
                    listAllSteps.map {
                        it.steps
                    }.reduce { pos1, pos2 -> pos1!! + pos2!! }

                val sumKcalInLifeTime =
                    listAllSteps.map {
                        it.calories
                    }.reduce { pos1, pos2 -> pos1!! + pos2!! }

                val sumKmInLifeTime = listAllSteps.map {
                    it.distance
                }.reduce { pos1, pos2 -> pos1!! + pos2!! }

                binding.tvTotalStepLifeTime.text = NumberFormat.getNumberInstance(Locale.US).format(sumStepInLifeTime)
                binding.tvTotalKcalLifeTime.text = String.format("%.3f",sumKcalInLifeTime).replace(",", ".")
                binding.tvTotalKmLifeTime.text = String.format("%.3f",sumKmInLifeTime).replace(",", ".")
            }
        }
        appViewModel.fetchItemStepsAll()
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.layoutLifeTime.setOnClickListener {
            findNavController().navigate(R.id.action_statisticsFragment_to_detailReportFragment)
        }
    }
}