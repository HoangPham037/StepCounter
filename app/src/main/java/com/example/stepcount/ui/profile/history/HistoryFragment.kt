package com.example.stepcount.ui.profile.history

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentHistoryBinding
import com.example.stepcount.utils.TrackingUtility

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(
    FragmentHistoryBinding::inflate
) {
    private lateinit var historyAdapter: HistoryAdapter
    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackHistory.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun getData() {
        super.getData()
        setupRecyclerview()
        appViewModel.itemHistory.observe(viewLifecycleOwner) { mListHistory ->
            var sumStep = 0
            var sumKcal = 0
            var sumTime = 0L
            var sumDistance = 0f
            if (mListHistory.isNotEmpty()) {
                historyAdapter.submitList(mListHistory)
                binding.recHistory.visibility = View.VISIBLE
                binding.vEmptyData.visibility = View.GONE
                mListHistory.forEach { historyData ->
                    sumStep += historyData.steps.toInt()
                    sumKcal += historyData.calories.toInt()
                    sumTime += historyData.time
                    sumDistance += historyData.distance
                }
                val time = TrackingUtility.getFormattedStopWatchTime(sumTime)
                setupViewBlockData(sumStep, sumKcal, time, sumDistance)
            } else {
                binding.vEmptyData.visibility = View.VISIBLE
                binding.recHistory.visibility = View.GONE
                val time = TrackingUtility.getFormattedStopWatchTime(sumTime)
                setupViewBlockData(sumStep, sumKcal, time, sumDistance)
            }
        }
        appViewModel.fetchItemHistory()
        setupRecyclerview()
    }

    private fun setupViewBlockData(
        sumStep: Int,
        sumKcal: Int,
        sumTime: String,
        sumDistance: Float
    ) {
        binding.tvSumStepHistory.text = sumStep.toString()
        binding.tvKmHistory.text = sumDistance.toString()
        binding.tvKcalHistory.text = sumKcal.toString()
        binding.tvTimeHistory.text = sumTime
    }

    private fun setupRecyclerview() = binding.recHistory.apply {
        historyAdapter = HistoryAdapter()
        adapter = historyAdapter
        layoutManager = LinearLayoutManager(requireContext())
        historyAdapter.onClickItem = { historyData ->
            val director = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailsFragment(
                historyData
            )
            findNavController().navigate(director)
        }

    }
}