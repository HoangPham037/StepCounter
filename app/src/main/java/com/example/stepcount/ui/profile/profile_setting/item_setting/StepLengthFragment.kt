package com.example.stepcount.ui.profile.profile_setting.item_setting

import android.content.SharedPreferences
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.Constant.KEY_STEPS
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentStepLengthBinding
import com.example.stepcount.ui.dialog.CustomDialog
import com.example.stepcount.ui.dialog.OnCustomDialogListener
import com.shawnlin.numberpicker.NumberPicker

/*
 created by Hoang.pv in 29/05.2023
 */
class StepLengthFragment : BaseFragment<FragmentStepLengthBinding>(
    FragmentStepLengthBinding::inflate
),OnCustomDialogListener {
    private val mStepLengthFragmentArgs: StepLengthFragmentArgs by navArgs()
    private var selected = 0
    companion object {
        const val minValue = 30
        const val maxValue = 149
    }
    override fun setupView() {
        super.setupView()
        binding.numberPicker.minValue = minValue
        binding.numberPicker.maxValue = maxValue
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
        val dialog = CustomDialog(requireContext())
        dialog.setCancelable(false)
        dialog.setCustomListener(this)
        dialog.show()
    }

    private fun updateStepLength() {
        if (selected != mStepLengthFragmentArgs.stepLength) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateStepLengthByUserId(userId.toLong(), selected)
        }
    }

    override fun onBtnYesListener() {
        updateStepLength()
    }

    override fun onNavigateToBack() {
        findNavController().navigateUp()
    }
}