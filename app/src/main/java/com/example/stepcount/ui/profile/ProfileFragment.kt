package com.example.stepcount.ui.profile

import androidx.appcompat.app.AppCompatDelegate
import com.example.stepcount.Constant.NavigateToNameFragment.FRAGMENT_HISTORY_NAME
import com.example.stepcount.Constant.NavigateToNameFragment.FRAGMENT_PROFILE_NAME
import com.example.stepcount.Constant.NavigateToNameFragment.FRAGMENT_YOUR_TARGET_NAME
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentProfileBinding
import com.example.stepcount.extension.changeFragmentAddToBackStacks
import com.example.stepcount.ui.profile.history.HistoryFragment
import com.example.stepcount.ui.profile.profile_setting.SettingProfileFragment
import com.example.stepcount.ui.profile.yourgoal.YourGoalFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {
    private var goals = 0
    private var nightMode: Boolean = false

    companion object {
        private var instance: ProfileFragment? = null
        fun newInstance(): ProfileFragment {
            if (instance == null) {
                instance = ProfileFragment()
            }
            return instance!!
        }
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.layoutProfileSetting.setOnClickListener {
            activity?.let {
                changeFragmentAddToBackStacks(
                    SettingProfileFragment(),
                    it.supportFragmentManager,
                    FRAGMENT_PROFILE_NAME
                )
            }
        }

        binding.layoutYourGoal.setOnClickListener {
            activity?.let {
                changeFragmentAddToBackStacks(
                    YourGoalFragment(),
                    it.supportFragmentManager,
                    FRAGMENT_HISTORY_NAME
                )
            }
        }

        binding.layoutHistoryGPS.setOnClickListener {
            activity?.let {
                changeFragmentAddToBackStacks(
                    HistoryFragment(),
                    it.supportFragmentManager,
                    FRAGMENT_YOUR_TARGET_NAME
                )
            }
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