package com.example.stepcount.ui.profile.update_profile.recommend

import androidx.fragment.app.viewModels
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentHeightsBinding
import com.example.stepcount.extension.changeFragment
import com.example.stepcount.ui.mainfragment.MainFragment
import com.example.stepcount.ui.profile.update_profile.RecommendFragment
import com.example.stepcount.ui.profile.update_profile.RecommendViewModel


class HeightsFragment : BaseFragment<FragmentHeightsBinding>(
    FragmentHeightsBinding::inflate
) {
    private val viewModel: RecommendViewModel by viewModels()

    companion object {
        private var instance: HeightsFragment? = null
        fun getInstance(): HeightsFragment {
            if (instance == null) {
                instance = HeightsFragment()
            }
            return instance!!
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        viewModel.height.observe(viewLifecycleOwner) {
            binding.numberPickerHeight.value = it
        }
        viewModel.height.observe(viewLifecycleOwner) { height ->
            height?.let { value ->
                appViewModel.registeredUserId.observe(viewLifecycleOwner) { userId ->
                    userId?.let {
                        appViewModel.updateHeightByUserId(it, value)
                    }
                }
            }
        }
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.tvSkip.setOnClickListener {
            activity?.let {
                changeFragment(MainFragment(), it.supportFragmentManager)
            }
        }

        binding.btnContinue.setOnClickListener {
            val mainFragment = parentFragment as? RecommendFragment
            mainFragment?.let {
                val viewPager = it.binding.viewpagerManagers
                viewPager.currentItem = Constant.IndexPage.indexThree
            }
        }

        binding.btnBack.setOnClickListener {
            val mainFragment = parentFragment as? RecommendFragment
            mainFragment?.let {
                val viewPager = it.binding.viewpagerManagers
                viewPager.currentItem = Constant.IndexPage.indexOne
            }
        }

        binding.numberPickerHeight.setOnValueChangedListener { picker, _, _ ->
            viewModel.setHeight(picker.value)
        }
    }
}