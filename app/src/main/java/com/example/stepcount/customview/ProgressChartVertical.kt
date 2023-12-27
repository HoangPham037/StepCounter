package com.example.stepcount.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.stepcount.R

class ProgressChartVertical : FrameLayout {
    private var mContext: Context? = null
    private var mMetrics: DisplayMetrics? = null
    private var mDataList: ArrayList<BarData>? = null
    private var listener: OnBarClickedListener? = null
    private val pins = ArrayList<TextView>()
    private var oldFrameLayout: FrameLayout? = null
    private var mEmptyColor = 0
    private var mProgressColor = 0
    private var mBarWidth = 0
    private var mBarHeight = 0
    private var mProgressClickColor = 0
    private var mBarRadius = 0
    private var mBarTitleMarginTop = 0
    private var mBarTitleSelectedColor = 0
    private var mProgressDisableColor = 0
    private var mPinTextColor = 0
    private var mPinBackgroundColor = 0
    private var mPinPaddingTop = 0
    private var mPinPaddingBottom = 0
    private var mPinPaddingEnd = 0
    private var mPinPaddingStart = 0
    private var mBarTitleColor = 0
    private var mBarTitleTxtSize = 0f
    private var isBarCanBeClick = false
    private var mMaxValue = 0f
    private var mPinTxtSize = 0f
    private var mPinMarginTop = 0
    private var mPinMarginBottom = 0
    private var mPinMarginEnd = 0
    private var mPinMarginStart = 0
    private var mPinDrawable = 0
    private var isBarsEmpty = false
    private var isOldBarClicked = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        setAttrs(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        setAttrs(attrs, defStyleAttr)
        mMetrics = Resources.getSystem().displayMetrics
    }

    private fun setAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray =
            mContext!!.obtainStyledAttributes(attrs, R.styleable.ChartProgressBar, defStyleAttr, 0)
        mBarWidth = typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_BarWidth, 0)
        mBarHeight = typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_BarHeight, 0)
        mBarRadius = typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_BarRadius, 0)
        mEmptyColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_EmptyColor,
            ContextCompat.getColor(mContext!!, R.color.white)
        )
        mProgressColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_ProgressColor,
            ContextCompat.getColor(mContext!!, R.color.bg_green)
        )
        mProgressClickColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_ProgressClickColor,
            ContextCompat.getColor(mContext!!, R.color.bg_blue)
        )
        mProgressDisableColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_ProgressDisableColor,
            ContextCompat.getColor(mContext!!, R.color.bg_gray)
        )
        mBarTitleSelectedColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_BarTitleSelectedColor,
            ContextCompat.getColor(mContext!!, R.color.bg_blue)
        )
        mPinTextColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_PinTextColor,
            ContextCompat.getColor(mContext!!, R.color.bg_orange)
        )
        mPinBackgroundColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_PinBackgroundColor,
            ContextCompat.getColor(mContext!!, R.color.bg_gray)
        )
        mPinPaddingTop =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinPaddingTop, 3)
        mPinPaddingBottom =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinPaddingBottom, 3)
        mPinPaddingEnd =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinPaddingEnd, 3)
        mPinPaddingStart =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinPaddingStart, 3)
        isBarCanBeClick = typedArray.getBoolean(R.styleable.ChartProgressBar_BarCanBeClick, false)
        mBarTitleColor = typedArray.getResourceId(
            R.styleable.ChartProgressBar_BarTitleColor,
            ContextCompat.getColor(mContext!!, R.color.black)
        )
        mMaxValue = typedArray.getFloat(R.styleable.ChartProgressBar_MaxValue, 1f)
        mBarTitleTxtSize =
            typedArray.getDimension(R.styleable.ChartProgressBar_BarTitleTxtSize, 14f)
        mPinTxtSize = typedArray.getDimension(R.styleable.ChartProgressBar_PinTxtSize, 14f)
        mPinMarginTop =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinMarginTop, 0)
        mPinMarginBottom =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinMarginBottom, 0)
        mPinMarginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinMarginEnd, 0)
        mPinMarginStart =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_PinMarginStart, 0)
        mBarTitleMarginTop =
            typedArray.getDimensionPixelSize(R.styleable.ChartProgressBar_BarTitleMarginTop, 0)
        typedArray.recycle()
    }

    fun setDataList(dataList: ArrayList<BarData>) {
        this.mDataList = dataList
    }

    fun build() {
        removeAllViews()
        val linearLayout = LinearLayout(mContext)
        val params = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        linearLayout.layoutParams = params
        addView(linearLayout)
        for ((i, data) in mDataList!!.withIndex()) {
            val barValue = (data.barValue * 100).toInt()
            val bar: FrameLayout? = getBar(data.barTitle, barValue, i)
            linearLayout.addView(bar)
        }
        viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver
                        .removeOnGlobalLayoutListener(this)
                    setPins()
                }
            })
    }

    private fun getBar(title: String, value: Int, index: Int): FrameLayout? {
        val maxValue = (mMaxValue * 100).toInt()
        val linearLayout = LinearLayout(mContext)
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        linearLayout.layoutParams = params
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER

        //Adding bar
        val bar = Bar(mContext, null, android.R.attr.progressBarStyleHorizontal)
        bar.progress = value
        bar.visibility = VISIBLE
        bar.isIndeterminate = false
        bar.max = maxValue
        bar.progressDrawable = ContextCompat.getDrawable(mContext!!, R.drawable.progress_bar_shape)
        val progressParams = LayoutParams(
            mBarWidth,
            mBarHeight
        )
        progressParams.gravity = Gravity.CENTER
        bar.layoutParams = progressParams
        val anim = BarAnimation(bar, 0f, value.toFloat())
        anim.duration = 1500
        bar.startAnimation(anim)
        val layerDrawable = bar.progressDrawable as LayerDrawable
        layerDrawable.mutate()
        val emptyLayer = layerDrawable.getDrawable(0) as GradientDrawable
        val scaleDrawable = layerDrawable.getDrawable(1) as ScaleDrawable
        emptyLayer.setColor(ContextCompat.getColor(mContext!!, mEmptyColor))
        emptyLayer.cornerRadius = mBarRadius.toFloat()
        val progressLayer = scaleDrawable.drawable as GradientDrawable?
        if (progressLayer != null) {
            progressLayer.setColor(ContextCompat.getColor(mContext!!, mProgressColor))
            progressLayer.cornerRadius = mBarRadius.toFloat()
        }
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(bar)

        //Adding txt below bar
        val txtBar = TextView(mContext)
        val txtParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        txtBar.text = title
        txtBar.gravity = Gravity.CENTER
        txtBar.layoutParams = txtParams
        linearLayout.addView(txtBar)


        val rootFrameLayout = FrameLayout(mContext!!)
        val rootParams = LinearLayout.LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            1f
        )
        rootParams.gravity = Gravity.CENTER

        rootFrameLayout.layoutParams = rootParams

        //Adding bar + title
        rootFrameLayout.addView(linearLayout)
        if (isBarCanBeClick) rootFrameLayout.setOnClickListener(barClickListener)
        rootFrameLayout.tag = index
        return rootFrameLayout
    }

    private fun setPins() {
        pins.clear()
        var childCount = childCount
        var linearLayout: LinearLayout? = null
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is LinearLayout) {
                linearLayout = view
                break
            }
        }
        if (linearLayout != null) {
            childCount = linearLayout.childCount
            for (i in 0 until childCount) {
                val view = linearLayout.getChildAt(i)
                val data: BarData = mDataList!![i]
                val pinTxt = data.pinText
                val barFrame = view as FrameLayout
                val frameCount = barFrame.childCount
                for (j in 0 until frameCount) {
                    val v = barFrame.getChildAt(j)
                    if (v is LinearLayout) {
                        val count = v.childCount
                        for (k in 0 until count) {
                            if (v.getChildAt(k) is Bar) {

                                // Adding value Txt when click on a bar
                                val pinTxtView = TextView(mContext)
                                val valueParams = LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                val pinDrawableId =
                                    if (mPinDrawable != 0) mPinDrawable else R.drawable.pin_shape
                                pinTxtView.setBackgroundResource(pinDrawableId)
                                pinTxtView.setPadding(
                                    mPinPaddingStart,
                                    mPinPaddingTop,
                                    mPinPaddingEnd,
                                    mPinPaddingBottom
                                )
                                pinTxtView.setTextColor(
                                    ContextCompat.getColor(
                                        mContext!!,
                                        mPinTextColor
                                    )
                                )
                                val bounds = Rect()
                                pinTxtView.text = pinTxt
                                pinTxtView.maxLines = 1
                                pinTxtView.gravity = Gravity.CENTER
                                val textPaint: Paint = pinTxtView.paint
                                textPaint.getTextBounds(pinTxt, 0, pinTxt.length, bounds)
                                val pinBackgroundWidth = bounds.width()
                                val x =
                                    (view.getX() - pinBackgroundWidth / 2 + view.getMeasuredWidth() / 2).toInt()
                                val y = view.getY().toInt()
                                pinTxtView.layoutParams = valueParams
                                pinTxtView.x = (x + mPinMarginStart - mPinMarginEnd).toFloat()
                                pinTxtView.y =
                                    (y + mPinMarginTop - mPinMarginBottom).toFloat()
                                addView(pinTxtView)
                                pins.add(pinTxtView)
                                pinTxtView.visibility = INVISIBLE
                                pinTxtView.tag = i
                            }
                        }
                    }
                }
            }
        }
    }

    private val barClickListener =
        OnClickListener { view ->
            if (isBarsEmpty) return@OnClickListener
            val frameLayout = view as FrameLayout
            if (oldFrameLayout === frameLayout) {
                if (isOldBarClicked) clickBarOff(frameLayout) else clickBarOn(frameLayout)
            } else {
                if (oldFrameLayout != null) clickBarOff(oldFrameLayout!!)
                clickBarOn(frameLayout)
            }
            oldFrameLayout = frameLayout
            if (listener != null) listener!!.onBarClicked(frameLayout.tag as Int)
        }

    fun setMaxValue(mMaxValue: Float) {
        this.mMaxValue = mMaxValue
    }

    private fun clickBarOn(frameLayout: FrameLayout) {
        pins[frameLayout.tag as Int].visibility = VISIBLE
        isOldBarClicked = true
        val childCount = frameLayout.childCount
        for (i in 0 until childCount) {
            val childView = frameLayout.getChildAt(i)
            if (childView is LinearLayout) {
                val bar = childView.getChildAt(0) as Bar
                val titleTxtView = childView.getChildAt(1) as TextView
                val layerDrawable = bar.progressDrawable as LayerDrawable
                layerDrawable.mutate()
                val scaleDrawable = layerDrawable.getDrawable(1) as ScaleDrawable
                val progressLayer = scaleDrawable.drawable as GradientDrawable?
                if (mPinBackgroundColor != 0) {
                    progressLayer?.setColor(ContextCompat.getColor(mContext!!, mProgressClickColor))
                } else {
                    progressLayer?.setColor(
                        ContextCompat.getColor(
                            mContext!!,
                            android.R.color.background_dark
                        )
                    )
                }
                if (mBarTitleSelectedColor > 0) {
                    titleTxtView.setTextColor(
                        ContextCompat.getColor(
                            mContext!!,
                            mBarTitleSelectedColor
                        )
                    )
                } else {
                    titleTxtView.setTextColor(
                        ContextCompat.getColor(
                            mContext!!,
                            android.R.color.background_dark
                        )
                    )
                }
            }
        }
    }

    private fun clickBarOff(frameLayout: FrameLayout) {
        pins[frameLayout.tag as Int].visibility = INVISIBLE
        isOldBarClicked = false
        val childCount = frameLayout.childCount
        for (i in 0 until childCount) {
            val childView = frameLayout.getChildAt(i)
            if (childView is LinearLayout) {
                val bar = childView.getChildAt(0) as Bar
                val layerDrawable = bar.progressDrawable as LayerDrawable
                layerDrawable.mutate()
                val scaleDrawable = layerDrawable.getDrawable(1) as ScaleDrawable
                val progressLayer = scaleDrawable.drawable as GradientDrawable?
                progressLayer?.setColor(ContextCompat.getColor(mContext!!, mProgressColor))
            }
        }
    }
}
interface OnBarClickedListener {
    fun onBarClicked(index: Int)
}