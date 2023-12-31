package com.example.stepcount.ui.profile.profile_setting

import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant.KEY_USER_ID
import com.example.stepcount.Constant.VALUE_DEFAULT
import com.example.stepcount.Constant.formatString
import com.example.stepcount.Constant.formatStringWeight
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentSettingProfileBinding

@Suppress("UNUSED_EXPRESSION")
class SettingProfileFragment : BaseFragment<FragmentSettingProfileBinding>(
    FragmentSettingProfileBinding::inflate
) {
    private var genders: String = ""
    private var steps: Int = 0
    private var heights: Int = 0
    private var weights: Int = 0

    override fun setupDataObserver() {
        super.setupDataObserver()
        getUserById()
    }
    private fun getUserById() {
        appViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            with(user) {
                genders = gender.toString()
                steps = stepLength!!
                heights = height!!
                weights = weight!!
                initUi(genders, steps, heights, weights)
            }
        }
        appViewModel.registeredUserId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                appViewModel.getUserById(userId.toLong())
            }
        }
    }

    private fun initUi(gender: String, step: Int, height: Int, weight: Int) {
        binding.tvGender.text = gender
        binding.tvStepsLength.text = formatString(step, "Cm")
        binding.tvHeight.text = formatString(height, "Cm")
        binding.tvWeight.text = formatStringWeight(weight, "Kg")
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {

            findNavController().popBackStack()
        }
        binding.layoutStepLength.setOnClickListener {
            navigateStepLengthFragment()
        }
        binding.layoutHeight.setOnClickListener {
            navigateHeightFragment()
        }
        binding.layoutWeight.setOnClickListener {
            navigateWeightFragment()
        }
        binding.layoutGender.setOnClickListener {
            navigateGenderFragment()
        }
    }

    private fun navigateHeightFragment() {
        val actionSettingProfileFragmentDirectionsHeight =
            SettingProfileFragmentDirections.actionSettingProfileFragmentToHeightFragment(heights)
        findNavController().navigate(actionSettingProfileFragmentDirectionsHeight)
    }

    private fun navigateWeightFragment() {
        val actionSettingProfileFragmentDirectionsWeight =
            SettingProfileFragmentDirections.actionSettingProfileFragmentToWeightFragment(weights)
        findNavController().navigate(actionSettingProfileFragmentDirectionsWeight)
    }

    private fun navigateStepLengthFragment() {
        val actionSettingProfileFragmentDirectionsStep =
            SettingProfileFragmentDirections.actionSettingProfileFragmentToStepLengthFragment(steps)
        findNavController().navigate(actionSettingProfileFragmentDirectionsStep)
    }

    private fun navigateGenderFragment() {
        val actionSettingProfileFragmentDirectionsGender =
            SettingProfileFragmentDirections.actionSettingProfileFragmentToGenderFragment(genders)
        findNavController().navigate(actionSettingProfileFragmentDirectionsGender)
    }

}