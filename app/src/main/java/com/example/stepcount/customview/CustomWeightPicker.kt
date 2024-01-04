package com.example.stepcount.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView

class CustomWeightPicker constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val kilogramPicker: NumberPicker
    private val gramPicker: NumberPicker

    init {
        orientation = HORIZONTAL

        kilogramPicker = NumberPicker(context)
        kilogramPicker.minValue = 0
        kilogramPicker.maxValue = 100
        kilogramPicker.wrapSelectorWheel = false

        gramPicker = NumberPicker(context)
        gramPicker.minValue = 0
        gramPicker.maxValue = 999
        gramPicker.wrapSelectorWheel = false

        val layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f)

        // Add kilogram picker
        addView(kilogramPicker, layoutParams)

        // Add gram picker
        addView(gramPicker, layoutParams)

        // Optionally, you can add labels for kilogram and gram
        val kilogramLabel = TextView(context)
        kilogramLabel.text = "kg"
        addView(kilogramLabel)

        val gramLabel = TextView(context)
        gramLabel.text = "g"
        addView(gramLabel)
    }

    fun getSelectedWeight(): Pair<Int, Int> {
        val kilogram = kilogramPicker.value
        val gram = gramPicker.value
        return Pair(kilogram, gram)
    }
}
