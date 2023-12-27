package com.example.stepcount.ui.profile.profile_setting.item_setting

import android.app.AlertDialog
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentGenderBinding

/*
 created by Hoang.pv in 29/05.2023
 */

class GenderFragment : BaseFragment<FragmentGenderBinding>(
    FragmentGenderBinding::inflate
) {
    private val mGenderFragmentArgs: GenderFragmentArgs by navArgs()
    private var gender: String? = null
    private val genderArray = arrayOf("Male", "FeMale", "Others")

    override fun setupView() {
        super.setupView()
        binding.numberPicker.minValue = 0
        binding.numberPicker.maxValue = 2
        binding.numberPicker.displayedValues = genderArray
        gender = mGenderFragmentArgs.gender
        val positionGender = genderArray.indexOf(gender)
        binding.numberPicker.value = positionGender
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            if (gender != mGenderFragmentArgs.gender) {
                showDialogAlertConfirm()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.numberPicker.setOnValueChangedListener { picker, _, _ ->
            gender = genderArray[picker.value]
            binding.tvSave.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    updateGender()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showDialogAlertConfirm() {
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Do you want to save?")
        dialogShowAlert.setPositiveButton("SAVE") { _, _ ->
            updateGender()
            findNavController().navigateUp()
        }
        dialogShowAlert.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.cancel()
            findNavController().navigateUp()
        }
        dialogShowAlert.create().show()
    }

    private fun updateGender() {
        if (gender != mGenderFragmentArgs.gender) {
            val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
            appViewModel.updateGenderByUserId(userId.toLong(), gender!!)

        }
    }
}