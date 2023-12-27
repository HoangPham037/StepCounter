package com.example.stepcount.ui.profile.yourgoal

import android.app.AlertDialog
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentYourGoalBinding

class YourGoalFragment : BaseFragment<FragmentYourGoalBinding>(
    FragmentYourGoalBinding::inflate
) {

    private val mYourGoalFragmentArgs: YourGoalFragmentArgs by navArgs()
    private var selected = 0
    override fun setupView() {
        super.setupView()
        binding.numberPicker.minValue = 0
        binding.numberPicker.maxValue = 50000
        selected = mYourGoalFragmentArgs.goal
        binding.numberPicker.value = selected

    }

    override fun initEventOnClick() {

        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            showDialogAlertConfirm()
        }
        binding.numberPicker.setOnValueChangedListener { picker, _, _ ->
            selected = picker.value
            binding.numberPicker.value = selected
            binding.tvSave.apply {
                visibility = View.VISIBLE

                setOnClickListener {
                    updateGoal()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showDialogAlertConfirm() {
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Do you want to save?")
        dialogShowAlert.setPositiveButton("SAVE") { _, _ ->
            updateGoal()
            findNavController().navigateUp()
        }
        dialogShowAlert.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.cancel()
            findNavController().navigateUp()
        }
        dialogShowAlert.create().show()
    }

    private fun updateGoal() {
        if (selected != mYourGoalFragmentArgs.goal) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateGoalByUserId(userId.toLong(), selected)
        }
    }
}