package com.example.stepcount.ui.profile.profile_setting.item_setting

import android.app.AlertDialog
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentWeightBinding

/*
 created by Hoang.pv in 29/05.2023
 */
class WeightFragment : BaseFragment<FragmentWeightBinding>(
    FragmentWeightBinding::inflate
) {

    private val mWeightFragmentArgs: WeightFragmentArgs by navArgs()
    private var selected = 0
    override fun setupView() {
        super.setupView()
        binding.numberPickerWeight.minValue = 1
        binding.numberPickerWeight.maxValue = 1200
        selected = mWeightFragmentArgs.weight
        binding.numberPickerWeight.value = selected
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            if (selected != mWeightFragmentArgs.weight) {
                showDialogAlertConfirm()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.numberPickerWeight.setOnValueChangedListener { picker, _, _ ->
            selected = picker.value
            binding.numberPickerWeight.value = selected
            binding.tvSave.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    updateWeight()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showDialogAlertConfirm() {
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Do you want to save?")
        dialogShowAlert.setPositiveButton("SAVE") { _, _ ->
            updateWeight()
            findNavController().navigateUp()
        }
        dialogShowAlert.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.cancel()
            findNavController().navigateUp()
        }
        dialogShowAlert.create().show()
    }

    private fun updateWeight() {
        if (selected != mWeightFragmentArgs.weight) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateWeightByUserId(userId.toLong(), selected)
        }
    }
}