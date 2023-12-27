package com.example.stepcount.ui.profile.update_profile

import android.graphics.Typeface
import android.widget.Button
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.MyOptionsPickerView
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.data.model.User
import com.example.stepcount.databinding.FragmentRecommendProfileBinding

class RecommendProfileFragment : BaseFragment<FragmentRecommendProfileBinding>(
    FragmentRecommendProfileBinding::inflate
) {
    private lateinit var singlePicker: MyOptionsPickerView<String>
    private var stepSelectedPosition = 119
    private var heightSelectedPosition = 168
    private var weightSelectedPosition = 699
    private var getValueOfRadioGroup: String = "Male"

    companion object {
        val STEP_RANGE = 1..150
        val HEIGHT_RANGE = 1..230
        val WEIGHT_RANGE = 1..1200
        const val GOAL_DEFAULT = 5000
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        singlePicker = MyOptionsPickerView(context)
        binding.blockDialogStepLength.setOnClickListener {
            val listItems = ArrayList(STEP_RANGE.map { it.toString() })
            baseDialogPicker(listItems)
            singlePicker.setOnoptionsSelectListener { options, _, _ ->
                stepSelectedPosition = options
                val step = listItems[options]
                binding.tvValueStepLength.text = step
            }
            singlePicker.setSelectOptions(stepSelectedPosition)
            singlePicker.show()
        }

        binding.blockDialogHeight.setOnClickListener {
            val listItems = ArrayList(HEIGHT_RANGE.map { it.toString() })
            baseDialogPicker(listItems)
            singlePicker.setOnoptionsSelectListener { options, _, _ ->
                heightSelectedPosition = options
                val height = listItems[options]
                binding.tvValueHeight.text = height
            }
            singlePicker.setSelectOptions(heightSelectedPosition)
            singlePicker.show()
        }

        binding.blockDialogWeight.setOnClickListener {
            val listItems = ArrayList(WEIGHT_RANGE.map { it.toString() })
            baseDialogPicker(listItems)
            singlePicker.setOnoptionsSelectListener { options, _, _ ->
                weightSelectedPosition = options
                val weight = listItems[options]
                binding.tvValueWeight.text = weight
            }

            singlePicker.setSelectOptions(weightSelectedPosition)
            singlePicker.show()
        }

        binding.tvSkipSetting.setOnClickListener {
            registerUser()
        }
        val radioGroup = binding.RadioGroup
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedRadioButton =
            binding.root.findViewById<RadioButton>(selectedRadioButtonId)

        if (selectedRadioButton != null) {
            getValueOfRadioGroup = selectedRadioButton.text.toString()
        }
        binding.btnStart.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val newUser = User(
            getValueOfRadioGroup,
            stepSelectedPosition.plus(1),
            heightSelectedPosition.plus(1),
            weightSelectedPosition.plus(1),
            GOAL_DEFAULT
        )
        appViewModel.saveUser(newUser)
        navigateToHome()
    }

    private fun baseDialogPicker(mList: ArrayList<String>) {
        singlePicker.apply {
            setPicker(mList)
            setCyclic(false)
            setCancelable(false)
            setCancelButtonText("Cancel")
            setSubmitButtonText("Save")
            (btnSubmit as? Button)?.apply {
                textSize = 16F
                isAllCaps = false
                setTypeface(null, Typeface.BOLD)
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bg_blue
                    )
                )
            }

            (btnCancel as? Button)?.apply {
                textSize = 16F
                isAllCaps = false
                setTypeface(null, Typeface.BOLD)
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_text_title
                    )
                )
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_recommendProfileFragment_to_homeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        singlePicker.dismiss()
    }
}