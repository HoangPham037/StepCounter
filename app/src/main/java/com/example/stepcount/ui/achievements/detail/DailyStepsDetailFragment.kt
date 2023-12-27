package com.example.stepcount.ui.achievements.detail

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.Constant
import com.example.stepcount.R
import com.example.stepcount.ShareDataViewModel
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentDailyStepsDetailBinding
import com.example.stepcount.ui.achievements.AchievementsViewModel
import com.example.stepcount.ui.achievements.adapter.DailyStepAdapter


class DailyStepsDetailFragment : BaseFragment<FragmentDailyStepsDetailBinding>(
    FragmentDailyStepsDetailBinding::inflate
), ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        private const val LEVEL_0 = 0f
        private const val LEVEL_1 = 3f
        private const val LEVEL_2 = 7f
        private const val LEVEL_3 = 10f
        private const val LEVEL_4 = 14f
        private const val LEVEL_5 = 20f
        private const val LEVEL_6 = 30f
        private const val LEVEL_7 = 40f
        private const val LEVEL_8 = 50f
        private const val LEVEL_9 = 60f
    }

    private val shareDataViewModel: ShareDataViewModel by navGraphViewModels(R.id.nav_graph)
    private val achievementsViewModel: AchievementsViewModel by viewModels()
    private lateinit var dailyStepAdapter: DailyStepAdapter
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
            findNavController().navigateUp()
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        shareDataViewModel.currentStepCounter.observe(viewLifecycleOwner) { step ->
            setUpDailyStep(step)

            step?.let { temps ->
                if (temps > 0) {
                    var value = temps / 1000
                    value = 60f
                    for (level in 0 until listValueLevel.size) {
                        if (level + 1 >= listValueLevel.size && value >= listValueLevel.last()) {
                            setLevelFinish(value.toInt())
                        } else if (value >= listValueLevel[level] && value <= listValueLevel[level + 1]) {
                            setLevelAndProgress(
                                value.toInt(),
                                level + 1,
                                listValueLevel[level + 1].toInt()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setUpDailyStep(step: Float) {
        achievementsViewModel.dataDailySteps.observe(viewLifecycleOwner) {
            dailyStepAdapter =
                DailyStepAdapter(requireContext(), it, step, LayoutParams.MATCH_PARENT)

            val itemDecoration = object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val height = parent.height
                    val heightOfItem = height / 3 - 35

                    val layoutParams = view.layoutParams
                    layoutParams.height = heightOfItem

                    view.layoutParams = layoutParams
                }
            }

            binding.rcDailyStep.addItemDecoration(itemDecoration)

            binding.rcDailyStep.adapter = dailyStepAdapter
        }
        achievementsViewModel.fetchListDailyStep()

    }

    private fun setLevelAndProgress(
        temps: Int,
        level: Int,
        maxValue: Int,
    ) {
        val temp = maxValue - temps
        binding.tvProgress.text = Constant.replaceString("${temp}K", level)
        binding.progress.apply {
            max = maxValue
            progress = temps
        }
        binding.tvDailyStep.text = maxValue.toString()
    }

    private fun setLevelFinish(maxValue: Int) {
        binding.tvDailyStep.text = maxValue.toString()
        binding.tvProgress.apply {
            text = "Finish"
            textSize = 30f
        }
        binding.progress.visibility = View.GONE
    }

    override fun onGlobalLayout() {
        // do nothing
    }
}