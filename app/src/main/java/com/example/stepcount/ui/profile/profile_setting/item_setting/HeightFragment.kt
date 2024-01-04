package com.example.stepcount.ui.profile.profile_setting.item_setting

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentHeightBinding
import com.example.stepcount.ui.dialog.CustomDialog
import com.example.stepcount.ui.dialog.OnCustomDialogListener

/*
 created by Hoang.pv in 29/05.2023
 */

class HeightFragment : BaseFragment<FragmentHeightBinding>(
    FragmentHeightBinding::inflate
), OnCustomDialogListener {
    private val mHeightFragmentArgs: HeightFragmentArgs by navArgs()
    private var selected = 0

    companion object {
        const val minValue = 1
        const val maxValue = 230
    }

    override fun setupView() {
        super.setupView()
        binding.numberPickerHeight.minValue = minValue
        binding.numberPickerHeight.maxValue = maxValue
        selected = mHeightFragmentArgs.height
        binding.numberPickerHeight.value = selected
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            if (selected != mHeightFragmentArgs.height) {
                showDialogAlertConfirm()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.numberPickerHeight.setOnValueChangedListener { picker, _, _ ->
            selected = picker.value
            binding.numberPickerHeight.value = selected
            binding.tvSave.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    updateHeight()
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

    private fun updateHeight() {
        if (selected != mHeightFragmentArgs.height) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateHeightByUserId(userId.toLong(), selected)
        }
    }

    override fun onBtnYesListener() {
        updateHeight()
    }

    override fun onNavigateToBack() {
        findNavController().navigateUp()
    }
}