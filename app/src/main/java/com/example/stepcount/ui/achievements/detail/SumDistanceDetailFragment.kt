package com.example.stepcount.ui.achievements.detail

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.stepcount.Constant
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentSumDistanceDetailBinding
import com.example.stepcount.ui.achievements.AchievementsViewModel
import com.example.stepcount.ui.achievements.adapter.DistanceAdapter
import com.example.stepcount.ShareDataViewModel


class SumDistanceDetailFragment : BaseFragment<FragmentSumDistanceDetailBinding>(
    FragmentSumDistanceDetailBinding::inflate
) {
    private val shareDataViewModel: ShareDataViewModel by navGraphViewModels(R.id.nav_graph)
    private val achievementsViewModel: AchievementsViewModel by viewModels()
    private lateinit var distanceAdapter: DistanceAdapter

    companion object {
        private const val LEVEL_0 = 0
        private const val LEVEL_1 = 5
        private const val LEVEL_2 = 10
        private const val LEVEL_3 = 20
        private const val LEVEL_4 = 50
        private const val LEVEL_5 = 100
        private const val LEVEL_6 = 220
        private const val LEVEL_7 = 440
        private const val LEVEL_8 = 800
        private const val LEVEL_9 = 2000
    }

    private val lisValueLevel = arrayListOf(
        LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6, LEVEL_7,
        LEVEL_8, LEVEL_9
    )

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpDistance(distance: Float) {
        achievementsViewModel.dataSumDistance.observe(viewLifecycleOwner) {
            distanceAdapter =
                DistanceAdapter(
                    requireContext(),
                    it,
                    distance,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
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
            binding.rcDistance.addItemDecoration(itemDecoration)
            binding.rcDistance.adapter = distanceAdapter
        }
        achievementsViewModel.fetchListDistance()

    }

    private fun setLevelAndProgress(
        temps: Int,
        level: Int,
        maxValue: Int,
    ) {
        val temp = maxValue - temps
        binding.tvProgress.text = Constant.replaceString("${temp}Km", level)
        binding.progress.apply {
            max = maxValue
            progress = temps
        }
        binding.tvDistance.text = maxValue.toString()
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        shareDataViewModel.distance.observe(viewLifecycleOwner) { distance ->
            setUpDistance(distance)
            distance?.let { temps ->
                for (level in 0 until lisValueLevel.size) {
                    if (level + 1 >= lisValueLevel.size && temps >= lisValueLevel.last()) {
                        binding.tvProgress.text = "Finish"
                        binding.progress.visibility = View.GONE
                        binding.tvDistance.text = "Level Up"
                    } else if (temps >= lisValueLevel[level] && temps <= lisValueLevel[level + 1]) {
                        setLevelAndProgress(temps.toInt(), level + 1, lisValueLevel[level + 1])
                    }
                }
            }
        }
    }
}