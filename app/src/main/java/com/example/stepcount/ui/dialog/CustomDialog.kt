package com.example.stepcount.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.stepcount.databinding.CustomDialogLayoutBinding

class CustomDialog(context: Context) : Dialog(context) {
    private lateinit var bindingDialog : CustomDialogLayoutBinding
    private var dialogListener: OnCustomDialogListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDialog = CustomDialogLayoutBinding.inflate(layoutInflater)
        setContentView(bindingDialog.root)
        initClick()
    }

    fun setCustomListener(dialogListener: OnCustomDialogListener) {
        this.dialogListener = dialogListener
    }

    private fun initClick() {
        bindingDialog.btnCloseDialog.setOnClickListener {
            dismiss()
        }
        bindingDialog.btnNo.setOnClickListener {
            dialogListener?.onNavigateToBack()
            dismiss()
        }
        bindingDialog.btnYes.setOnClickListener {
            dialogListener?.onBtnYesListener()
            dialogListener?.onNavigateToBack()
            dismiss()
        }
    }
}

interface OnCustomDialogListener {
    fun onBtnYesListener()
    fun onNavigateToBack()
}