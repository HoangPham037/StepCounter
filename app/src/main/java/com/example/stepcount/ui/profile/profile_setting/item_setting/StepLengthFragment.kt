package com.example.stepcount.ui.profile.profile_setting.item_setting

import android.app.AlertDialog
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentStepLengthBinding

/*
 created by Hoang.pv in 29/05.2023
 */
class StepLengthFragment : BaseFragment<FragmentStepLengthBinding>(
    FragmentStepLengthBinding::inflate
) {
    private val mStepLengthFragmentArgs: StepLengthFragmentArgs by navArgs()
    private var selected = 0
    override fun setupView() {
        super.setupView()
        binding.numberPicker.minValue = 30
        binding.numberPicker.maxValue = 149
        selected = mStepLengthFragmentArgs.stepLength
        binding.numberPicker.value = selected
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            if (selected != mStepLengthFragmentArgs.stepLength) {
                showDialogAlertConfirm()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.numberPicker.setOnValueChangedListener { picker, _, _ ->
            selected = picker.value
            binding.numberPicker.value = selected
            binding.tvSave.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    updateStepLength()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showDialogAlertConfirm() {
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Do you want to save?")
        dialogShowAlert.setPositiveButton("Yes") { _, _ ->
            updateStepLength()
            findNavController().navigateUp()
        }
        dialogShowAlert.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
            findNavController().navigateUp()
        }
        dialogShowAlert.create().show()
    }

    private fun updateStepLength() {
        if (selected != mStepLengthFragmentArgs.stepLength) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateStepLengthByUserId(userId.toLong(), selected)
        }
    }
}