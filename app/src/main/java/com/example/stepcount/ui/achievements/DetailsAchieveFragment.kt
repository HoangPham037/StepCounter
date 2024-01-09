package com.example.stepcount.ui.achievements

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentDetailsAchieveBinding
import com.example.stepcount.ui.achievements.adapter.StepLevelAdapter


class DetailsAchieveFragment : BaseFragment<FragmentDetailsAchieveBinding>(
    FragmentDetailsAchieveBinding::inflate
) {

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
    }

    private val achievementsViewModel: AchievementsViewModel by viewModels()
    private lateinit var stepsLevelAdapter: StepLevelAdapter
    private val listValueLevel = arrayListOf(
        LEVEL_0,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        LEVEL_4,
        LEVEL_5,
        LEVEL_6,
        LEVEL_7,
        LEVEL_8,
        LEVEL_9
    )

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBack.setOnClickListener {
            activity?.let {
                it.supportFragmentManager.popBackStack()
            }
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        appViewModel.stepsAll.observe(viewLifecycleOwner) { list ->
            list?.let {
                val temp = it.map { steps ->
                    steps.steps
                }.reduce { acc, fl -> acc!! + fl!! }
                temp?.let { temps ->
                    setUpStepsLevel(temps)
                    for (level in 0 until listValueLevel.size) {
                        if (level + 1 >= listValueLevel.size && temp >= listValueLevel.last()) {
                            binding.tvProgress.text = "Finish"
                            binding.progress.visibility = View.GONE
                            binding.tvStepsLevel.text = "Level Up"
                        } else if (temps >= listValueLevel[level] && temp <= listValueLevel[level + 1]) {
                            setLevelAndProgress(temps.toInt(), level, listValueLevel[level + 1])
                        }
                    }
                }
            }
        }
        appViewModel.fetchItemStepsAll()
    }

    private fun setUpStepsLevel(steps: Float) {
        achievementsViewModel.dataListStepsLevel.observe(viewLifecycleOwner) {
            stepsLevelAdapter = StepLevelAdapter(it, requireContext(), steps)
            binding.rcLevelStep.adapter = stepsLevelAdapter
        }
        achievementsViewModel.fetchListStepsLevel()
    }

    private fun setLevelAndProgress(
        temps: Int,
        level: Int,
        maxValue: Int,
    ) {
        val temp = maxValue - temps
        binding.tvProgress.text = Constant.replaceString(Constant.formatNumber(temp), level + 1)
        binding.progress.apply {
            max = maxValue
            progress = temps
        }
        binding.tvStepsLevel.text = String.format("%s%d", "L", level)
    }
}