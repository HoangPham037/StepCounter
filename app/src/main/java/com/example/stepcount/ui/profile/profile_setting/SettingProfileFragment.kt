package com.example.stepcount.ui.profile.profile_setting

import androidx.navigation.fragment.findNavController
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentSettingProfileBinding

@Suppress("UNUSED_EXPRESSION")
class SettingProfileFragment : BaseFragment<FragmentSettingProfileBinding>(
    FragmentSettingProfileBinding::inflate
) {
    private var genders: String = ""
    private var steps: Double = 0.0
    private var heights: Int = 0
    private var weights: Double = 0.0

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

    private fun initUi(gender: String, step: Double, height: Int, weight: Double) {
        binding.tvGender.text = gender
        binding.tvStepsLength.text = String.format("%.1f%s", step, "Cm")
        binding.tvHeight.text = String.format("%d%s", height, "Cm")
        binding.tvWeight.text = String.format("%.1f%s", weight, "Kg")
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
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
            SettingProfileFragmentDirections.actionSettingProfileFragmentToWeightFragment(weights.toInt())
        findNavController().navigate(actionSettingProfileFragmentDirectionsWeight)
    }

    private fun navigateStepLengthFragment() {
        val actionSettingProfileFragmentDirectionsStep =
            SettingProfileFragmentDirections.actionSettingProfileFragmentToStepLengthFragment(steps.toInt())
        findNavController().navigate(actionSettingProfileFragmentDirectionsStep)
    }

    private fun navigateGenderFragment() {
        val actionSettingProfileFragmentDirectionsGender =
            SettingProfileFragmentDirections.actionSettingProfileFragmentToGenderFragment(genders)
        findNavController().navigate(actionSettingProfileFragmentDirectionsGender)
    }

}