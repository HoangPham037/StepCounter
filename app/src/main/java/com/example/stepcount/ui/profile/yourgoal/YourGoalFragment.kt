package com.example.stepcount.ui.profile.yourgoal

import android.app.AlertDialog
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentYourGoalBinding
import kotlin.math.min

class YourGoalFragment : BaseFragment<FragmentYourGoalBinding>(
    FragmentYourGoalBinding::inflate
) {

    companion object {
        const val increment = 500
        const val minValue = 500
        const val maxValue = 40000
    }
    val data = mutableListOf<String>()
    private val mYourGoalFragmentArgs: YourGoalFragmentArgs by navArgs()
    private var selected = 0
    override fun setupView() {
        super.setupView()

        for (value in minValue..maxValue step increment) {
            data.add(value.toString())
        }
        binding.numberPicker.wrapSelectorWheel = true
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = data.size
        binding.numberPicker.displayedValues = data.toTypedArray()

        selected = mYourGoalFragmentArgs.goal
//        binding.numberPicker.value = selected
        binding.numberPicker.value = 1

    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackSettingProfile.setOnClickListener {
            showDialogAlertConfirm()
        }
        binding.numberPicker.setOnValueChangedListener { picker, oldValue, newValue ->
//            selected = picker.value
            val pickerIndex = picker.value

                selected = data[pickerIndex].toInt()

                Log.d("555555", "Selected value: $selected")

                Log.d("555555", "picker.value: ${picker.value}")
                Log.d("555555", "oldValue: ${oldValue}")
                Log.d("555555", "newValue: ${newValue}")
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