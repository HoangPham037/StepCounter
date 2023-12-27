package com.example.stepcount.ui.profile

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant.KEY_USER_ID
import com.example.stepcount.Constant.VALUE_DEFAULT
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {
    private var goals = 0
    private var nightMode: Boolean = false

    companion object {
        fun newInstance(): ProfileFragment{
            return ProfileFragment()
        }
    }
    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.layoutProfileSetting.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingProfileFragment)
        }

        binding.layoutYourGoal.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_yourGoalFragment)
//            val actionYourGoalFragment =
//                ProfileFragmentDirections.actionProfileFragmentToYourGoalFragment(goals)
//            findNavController().navigate(actionYourGoalFragment)
        }

        binding.layoutHistoryGPS.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_historyFragment2)
        }

        nightMode = MyApplication.loadDataBoolean("night", false)

        if (nightMode) {
            binding.switcher.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.switcher.setOnClickListener {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                MyApplication.saveDataBoolean("night", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                MyApplication.saveDataBoolean("night", true)
            }
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        getInfoUser()
    }

    private fun getInfoUser() {
        appViewModel.registeredUserId.observe(viewLifecycleOwner) { userId ->
            appViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                with(user) {
                    goals = goal!!
                    binding.tvYourGoal.text = goals.toString()
                }
            }
            if (userId != null) {
                appViewModel.getUserById(userId.toLong())
            }
        }
    }
}