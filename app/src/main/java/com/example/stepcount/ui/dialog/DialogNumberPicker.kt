package com.example.stepcount.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.stepcount.R
import com.example.stepcount.databinding.BottomSheetLayoutBinding
import com.example.stepcount.ui.profile.update_profile.RecommendViewModel
import com.google.gson.annotations.Until

class DialogNumberPicker(
    private val minValue: Int,
    private val maxValue: Int,
    private val default: Int
) : DialogFragment() {

    private val recommendViewModel: RecommendViewModel by activityViewModels()
    private lateinit var bindingBottomSheet: BottomSheetLayoutBinding
    private var onNumberPickerDialog : OnNumberPickerDialog ?= null
    private var selected : Int?= 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingBottomSheet = BottomSheetLayoutBinding.inflate(layoutInflater)
        return bindingBottomSheet.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onNumberPickerDialog = activity as OnNumberPickerDialog
        bindingBottomSheet.numberPicker.minValue = minValue
        bindingBottomSheet.numberPicker.maxValue = maxValue
        bindingBottomSheet.numberPicker.value = default

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)

        bindingBottomSheet.numberPicker.setOnValueChangedListener { picker, _, _ ->
            selected = picker.value
        }

        bindingBottomSheet.tvSave.setOnClickListener {
            onNumberPickerDialog?.onSaveNumberPickerDialog(selected!!)
            this.dismiss()
        }

        bindingBottomSheet.tvCancel.setOnClickListener {
            this.dismiss()
        }
    }

    interface OnNumberPickerDialog{
        fun onSaveNumberPickerDialog(select:Int)
    }
}