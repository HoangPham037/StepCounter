package com.example.stepcount.ui.profile.profile_setting.item_setting

import android.app.AlertDialog
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentHeightBinding

/*
 created by Hoang.pv in 29/05.2023
 */

class HeightFragment : BaseFragment<FragmentHeightBinding>(
    FragmentHeightBinding::inflate
) {
    private val mHeightFragmentArgs: HeightFragmentArgs by navArgs()
    private var selected = 0
    override fun setupView() {
        super.setupView()
        binding.numberPickerHeight.minValue = 1
        binding.numberPickerHeight.maxValue = 230
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
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Do you want to save?")
        dialogShowAlert.setPositiveButton("SAVE") { _, _ ->
            updateHeight()
            findNavController().navigateUp()
        }
        dialogShowAlert.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.cancel()
            findNavController().navigateUp()
        }
        dialogShowAlert.create().show()
    }

    private fun updateHeight() {
        if (selected != mHeightFragmentArgs.height) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateHeightByUserId(userId.toLong(), selected)
        }
    }
}