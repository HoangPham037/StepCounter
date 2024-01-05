package com.example.stepcount.ui.profile.update_profile.recommend

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentWeightsBinding
import com.example.stepcount.ui.profile.update_profile.RecommendFragment
import com.example.stepcount.ui.profile.update_profile.RecommendFragmentDirections
import com.example.stepcount.ui.profile.update_profile.RecommendViewModel

class WeightsFragment : BaseFragment<FragmentWeightsBinding>(
    FragmentWeightsBinding::inflate
) {

    private val viewModel: RecommendViewModel by activityViewModels()

    companion object {
        private var instance: WeightsFragment? = null
        fun getInstance(): WeightsFragment {
            if (instance == null) {
                instance = WeightsFragment()
            }
            return instance!!
        }
    }

    override fun setData() {
        super.setData()
        binding.numberPickerWeightKg.setOnValueChangedListener { picker, _, _ ->
            viewModel.setWeightKg(picker.value.toString())
        }
        binding.numberPickerWeightG.setOnValueChangedListener { picker, _, _ ->
            viewModel.setWeightG(picker.value.toString())
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        viewModel.weightKg.observe(viewLifecycleOwner) {
            binding.numberPickerWeightKg.value = it.toInt()
        }
        viewModel.weightG.observe(viewLifecycleOwner) {
            binding.numberPickerWeightG.value = it.toInt()
        }
        viewModel.totalWeight.observe(viewLifecycleOwner) { weight ->
            val temp = weight.toDouble()
            appViewModel.registeredUserId.observe(viewLifecycleOwner) { userId ->
                if (userId != null) {
                    appViewModel.updateWeightByUserId(userId, temp)
                }
            }
        }
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_recommendFragment_to_mainFragment)
        }
        binding.btnGone.setOnClickListener {
            val action = RecommendFragmentDirections.actionRecommendFragmentToMainFragment()
            findNavController().navigate(action)
        }
        binding.btnBack.setOnClickListener {
            val recommendFragment = parentFragment as? RecommendFragment
            recommendFragment?.let {
                val viewPager = it.binding.viewpagerManagers
                viewPager.currentItem = Constant.IndexPage.indexTwo
            }
        }

    }
}