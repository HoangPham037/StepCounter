package com.example.stepcount.ui.achievements

import android.content.res.ColorStateList
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.stepcount.Constant
import com.example.stepcount.Constant.replaceString
import com.example.stepcount.R
import com.example.stepcount.ShareDataViewModel
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentAchievementsBinding
import com.example.stepcount.ui.achievements.adapter.DailyStepAdapter
import com.example.stepcount.ui.achievements.adapter.DistanceAdapter

class AchievementsFragment : BaseFragment<FragmentAchievementsBinding>(
    FragmentAchievementsBinding::inflate
) {

    private val achievementsViewModel: AchievementsViewModel by viewModels()
    private val shareDataViewModel: ShareDataViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var dailyStepAdapter: DailyStepAdapter
    private lateinit var distanceAdapter: DistanceAdapter
    private val listValueLevel = arrayListOf(
        LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5,
        LEVEL_5, LEVEL_6, LEVEL_7, LEVEL_8, LEVEL_9
    )

    companion object {
        private const val LEVEL_0 = 0
        private const val LEVEL_1 = 10000
        private const val LEVEL_2 = 50000
        private const val LEVEL_3 = 100000
        private const val LEVEL_4 = 150000
        private const val LEVEL_5 = 200000
        private const val LEVEL_6 = 400000
        private const val LEVEL_7 = 600000
        private const val LEVEL_8 = 1000000
        private const val LEVEL_9 = 1200000

        fun newInstance(): AchievementsFragment {
            return AchievementsFragment()
        }
    }


    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.tvSeeMoreYourLv.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_detailsAchieveFragment)
        }
        binding.tvSeeMoreDailySteps.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_dailyStepsDetailFragment)
        }

        binding.tvSeeMoreSumDistance.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sumDistanceDetailFragment)
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        appViewModel.stepsAll.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                setUpLevel(0f)
                setUpDistance(0f)
                setUpDailyStep(0f)
            } else {
                list?.let {
                    // sum steps
                    val temp = it.map { steps ->
                        steps.steps
                    }.fold(0f) { acc, fl -> acc + fl!! }

                    temp?.let { step ->

                        setUpLevel(step)
                    }
                    // sum distance
                    val tempDistance = it.map { steps ->
                        steps.distance
                    }.reduce { acc, fl -> acc!! + fl!! }
                    tempDistance?.let { distance ->
                        shareDataViewModel.distance.value = distance
                        setUpDistance(distance)
                    }
                }
            }

        }
        shareDataViewModel.currentStepCounter.observe(viewLifecycleOwner) { step ->
            setUpDailyStep(step)
        }
        appViewModel.fetchItemStepsAll()
    }

    private fun setUpDailyStep(step: Float) {
        achievementsViewModel.dataDailySteps.observe(viewLifecycleOwner) {
            dailyStepAdapter =
                DailyStepAdapter(requireContext(), it.take(4), step, LayoutParams.WRAP_CONTENT)
            binding.rcSumSteps.adapter = dailyStepAdapter
        }
        achievementsViewModel.fetchListDailyStep()
    }

    private fun setUpDistance(distance: Float) {
        achievementsViewModel.dataSumDistance.observe(viewLifecycleOwner) {
            distanceAdapter =
                DistanceAdapter(requireContext(), it.take(4), distance, LayoutParams.WRAP_CONTENT)
            binding.rcSumDistance.adapter = distanceAdapter
        }
        achievementsViewModel.fetchListDistance()
    }

    private fun setUpViewLevel(temps: Int, maxValue: Int, level: Int) {
        binding.progress.apply {
            max = maxValue
            progress = temps
            progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.bg_blue
                )
            )
        }
        val temp = maxValue - temps
        binding.tvProgress.text = replaceString(Constant.formatNumber(temp), level + 1)
        binding.tvStepsLevel.text = "L$level"
    }

    private fun setUpLevel(temps: Float) {

        for (level in 0 until listValueLevel.size) {
            if (level + 1 >= listValueLevel.size && temps >= listValueLevel.last()) {
                binding.tvProgress.text = "Finish"
                binding.tvStepsLevel.text = "Level Up"
                binding.progress.visibility = View.GONE
            } else if (temps >= listValueLevel[level] && temps <= listValueLevel[level + 1]) {
                setUpViewLevel(temps.toInt(), listValueLevel[level + 1], level + 1)
            }
        }
    }
}