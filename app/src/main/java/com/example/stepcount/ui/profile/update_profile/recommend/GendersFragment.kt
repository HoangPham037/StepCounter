package com.example.stepcount.ui.profile.update_profile.recommend

import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentGendersBinding
import com.example.stepcount.ui.profile.update_profile.RecommendFragment
import com.example.stepcount.ui.profile.update_profile.RecommendFragmentDirections
import com.example.stepcount.ui.profile.update_profile.RecommendViewModel


class GendersFragment : BaseFragment<FragmentGendersBinding>(
    FragmentGendersBinding::inflate
) {
    private val viewModel: RecommendViewModel by activityViewModels()
    private var getGender: String = "male"

    companion object {
        private var instance: GendersFragment? = null

        @JvmStatic
        fun getInstance(): GendersFragment {
            if (instance == null) {
                instance = GendersFragment()
            }
            return instance!!
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        viewModel.gender.observe(viewLifecycleOwner) {gender ->
            gender?.let {value->
                appViewModel.registeredUserId.observe(viewLifecycleOwner) {userId->
                    userId?.let {
                        appViewModel.updateGenderByUserId(it, value)
                    }
                }
            }

        }
    }
    override fun initEventOnClick() {
        super.initEventOnClick()
        val radioGroup = binding.radioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
            if (selectedRadioButton != null) {
                getGender = selectedRadioButton.text.toString()
                viewModel.setGender(getGender)
            }
        }
        binding.tvSkip.setOnClickListener {
            val action = RecommendFragmentDirections.actionRecommendFragmentToMainFragment()
            findNavController().navigate(action)
        }
        binding.btnContinue.setOnClickListener {
            viewModel.setGender(getGender)
            val mainFragment = parentFragment as? RecommendFragment
            mainFragment?.let {
                val viewPager = it.binding.viewpagerManagers
                viewPager.currentItem = Constant.IndexPage.indexTwo
            }
        }
    }

}