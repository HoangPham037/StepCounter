package com.example.stepcount.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.stepcount.Constant.IndexPage.indexFour
import com.example.stepcount.DateHelper
import com.example.stepcount.R
import com.example.stepcount.ShareDataViewModel
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.databinding.DialogSelectMoreBinding
import com.example.stepcount.databinding.FragmentHomeBinding
import com.example.stepcount.service.StepCountService
import com.example.stepcount.ui.home.adapter.SuccessAdapter
import com.example.stepcount.ui.mainfragment.MainFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.util.*


class HomeFragment :
    SensorEventListener,
    SharedPreferences.OnSharedPreferenceChangeListener,
    BaseFragment<FragmentHomeBinding>(
        FragmentHomeBinding::inflate
    ) {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private val shareDataViewModel: ShareDataViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var popupWindow: PopupWindow
    private var running = false
    private var previousTotalSteps = 0f
    private var isClickBtnPause = false
    private var isShowTvPause = false
    private var currentSteps = 0f
    private val successAdapter by lazy { SuccessAdapter() }
    private val viewModel: HomeViewModel by viewModels()
    private var state: Boolean = false

    private val calendar = Calendar.getInstance()
    private val day = calendar[Calendar.DAY_OF_MONTH]
    private val month = calendar[Calendar.MONTH] + 1
    private val year = calendar[Calendar.YEAR]
    private var currentDay: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.circleCenter.apply {
            setProgressWithAnimation(0f)
        }
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupView() {
        super.setupView()
        getCurrentTime()
        shareDataViewModel.currentStepCounter.observe(viewLifecycleOwner) { currentStep ->
            currentSteps = currentStep
            val calories = calculateCaloriesCovered(currentStep.toFloat())
            val distance = calculateDistance(currentStep.toFloat())
            val time = calculateTime(currentStep.toInt(), distance)
            binding.sumSteps.text = currentStep.toInt().toString()
            binding.sumCalories.text = formatDisplayFloat(calories, "kcal")
            binding.tvTime.text = formatDisplayFloat(time.toFloat(), "HH:mm")
            binding.tvDistance.text = formatDisplayFloat(distance, "Km")
            binding.circleCenter.apply {
                setProgressWithAnimation(currentStep.toFloat())
            }
        }
    }
    private fun getCurrentTime(){
        val currentTime = Calendar.getInstance().time
        val formatTime = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)
        Log.d("getCurrentTime", "getCurrentTime:  ${formatTime}")
        binding.tvToday.text = formatTime
    }

    private fun formatDisplayFloat(values: Float, unit: String): String {
        return String.format("%.2f %s", values, unit)
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.showButton.setOnClickListener {
            binding.showButton.visibility = View.GONE
            binding.hideButton.visibility = View.VISIBLE
            showPopupWindow(binding.showButton)
        }
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        getInfoUser()
        getSuccessStep()
    }

    private fun getSuccessStep() {
        binding.achievements.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.CENTER
        }
        appViewModel.step.observe(viewLifecycleOwner) { listStep ->
            if (!listStep.isNullOrEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.create(listStep as ArrayList<StepsData>).also { listSteps ->
                        successAdapter.setData(listSteps)
                        binding.achievements.adapter = successAdapter
                    }
                }
            } else {
                val listEmpty = arrayListOf(
                    StepsData(
                        0f,
                        DateHelper.name(day, month, year),
                        day,
                        month,
                        year,
                        0f,
                        0f,
                        0,
                        false
                    )
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.create(listEmpty).also {
                        successAdapter.setData(it)
                        binding.achievements.adapter = successAdapter
                    }
                }
            }
        }

        appViewModel.fetchItemSteps()
    }

    private fun getInfoUser() {
        appViewModel.registeredUserId.observe(viewLifecycleOwner) { userId ->
            appViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                with(user) {
                    val target = goal!!.toFloat()
                    viewModel.setTarget(target)
                    binding.tvProgress.text = formatDisplayFloat((currentSteps / target) * 100, "%")
                    binding.goalSteps.text = formatGoal(goal.toInt())
                    binding.circleCenter.progressMax = target
                }
            }
            if (userId != null) {
                appViewModel.getUserById(userId.toLong())
            }
        }
    }

    private fun formatGoal(goal: Int): String {
        return "$goal steps"
    }

    override fun onResume() {
        startForegroundService()
        super.onResume()
        if (sensor == null) {
            Snackbar.make(binding.root, "This device has no sensor", Snackbar.LENGTH_SHORT).show()
        } else {
            running = true
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }


//    private fun resetSteps() {
//        previousTotalSteps = totalSteps
//        binding.sumSteps.text = "0"
////        saveData()
//        binding.progressBarHorizontal.apply {
//            max = currentStepCounter
//            progress = currentStepCounter
//        }
//        binding.circleCenter.apply {
//            setProgressWithAnimation(0f)
//        }
//    }

    private fun loadData() {
        val previousStep = MyApplication.loadData("previousStep", 0f)
        previousTotalSteps = previousStep
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.d("onSensorChanged", "isRunning: $running ")
        if (running) {
            val totalSteps = p0!!.values[0]
            currentDay = DateHelper.name(day, month, year)

            val lastSavedDate = DateHelper.getLastDate()
            if (lastSavedDate != null && DateHelper.isSameDay(currentDay!!, lastSavedDate)) {
                loadData()
                shareDataViewModel.setCurrentStepCounter(totalSteps - previousTotalSteps)
                shareDataViewModel.setTotalStep(totalSteps)
                val currentStep = totalSteps.toInt() - previousTotalSteps.toInt()
                viewModel.target.observe(viewLifecycleOwner) { target ->
                    state = currentStep >= target
                }
            } else {
                loadData()
                val currentStep = totalSteps.toInt() - previousTotalSteps.toInt()
                viewModel.target.observe(viewLifecycleOwner) { target ->
                    state = currentStep > target
                    val stepsData = StepsData(
                        steps = currentStep.toFloat(),
                        nameDay = currentDay,
                        day = day,
                        month = month,
                        year = year,
                        distance = calculateDistance(currentStep.toFloat()),
                        calories = calculateCaloriesCovered(currentStep.toFloat()),
                        time = calculateTime(currentStep, calculateDistance(currentStep.toFloat())),
                        state = state
                    )
                    appViewModel.saveSteps(stepsData)
                }

                previousTotalSteps = totalSteps
                MyApplication.saveData("previousStep", previousTotalSteps)
            }
            DateHelper.saveLastDate(currentDay!!)
        } else {
            // Xử lý khi `running` là false
            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_MONTH]
            val month = calendar[Calendar.MONTH] + 1
            val year = calendar[Calendar.YEAR]
            val currentDay = DateHelper.name(day, month, year)

            val stepsData = StepsData(
                steps = 0f,
                nameDay = currentDay,
                day = day,
                month = month,
                year = year,
                distance = calculateDistance(0f),
                calories = calculateCaloriesCovered(0f),
                time = calculateTime(0, calculateDistance(0f)),
                state = false
            )
            appViewModel.saveSteps(stepsData)
            DateHelper.saveLastDate(currentDay)
        }
    }

    private fun calculateTime(steps: Int, distance: Float): Long {
        val stepRate = 1f
        val totalTimeInSecond = (steps / stepRate).toLong()

        val averageSpeeds = distance / totalTimeInSecond
        return (distance / averageSpeeds).toLong()
    }

    private fun calculateCaloriesCovered(steps: Float): Float {
        return ((steps * 0.033).toFloat())
    }

    private fun calculateDistance(steps: Float): Float {
        val feet = (steps * 2.1).toInt()
        val distance = (feet / 3.281) / 1000
        return distance.toFloat()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //do nothing
    }

    private fun showPopupWindow(anchorView: View) {
        val bindingDialog = DialogSelectMoreBinding.inflate(layoutInflater)
        val popupView = bindingDialog.root
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(anchorView)
        popupWindow.setOnDismissListener {
            binding.hideButton.visibility = View.GONE
            binding.showButton.visibility = View.VISIBLE
        }
        bindingDialog.btnOff.setOnClickListener {
            requireActivity().finish()
        }
        bindingDialog.btnPause.setOnClickListener {
            sensorManager?.unregisterListener(this, sensor)
            isClickBtnPause = true
            isShowTvPause = true
            saveLiveData()
            hidePopupWindow()
        }
        bindingDialog.btnRun.setOnClickListener {
            isClickBtnPause = false
            isShowTvPause = false
            saveLiveData()
            hidePopupWindow()
        }
        bindingDialog.btnReset.setOnClickListener {
            showDialogAlert()
            loadData()
            hidePopupWindow()
        }

        bindingDialog.btnPractice.setOnClickListener {
            hidePopupWindow()
            val mainFragment = parentFragment as? MainFragment
            mainFragment?.let {
                val viewPager = it.binding.viewpagerManager
                viewPager.currentItem = indexFour
            }
        }
        shareDataViewModel.isClickBtnPause.observe(viewLifecycleOwner) { isClick ->
            if (isClick == true) {
                bindingDialog.btnRun.visibility = View.VISIBLE
                bindingDialog.btnPause.visibility = View.GONE
                bindingDialog.tvPause.visibility = View.GONE
                bindingDialog.tvRun.visibility = View.VISIBLE
            } else {
                bindingDialog.btnRun.visibility = View.GONE
                bindingDialog.btnPause.visibility = View.VISIBLE
                bindingDialog.tvPause.visibility = View.VISIBLE
                bindingDialog.tvRun.visibility = View.GONE
            }
        }
    }

    private fun saveLiveData() {
        shareDataViewModel.setIsClickBtnPause(isClickBtnPause)
        shareDataViewModel.setIsShowTvPause(isShowTvPause)
        binding.hideButton.visibility = View.GONE
        binding.showButton.visibility = View.VISIBLE
    }

    private fun showDialogAlert() {
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Are you sure you want to reset the step count?")
        dialogShowAlert.setPositiveButton("Yes") { _, _ ->
//            resetSteps()
        }
        dialogShowAlert.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        dialogShowAlert.create().show()

    }

    private fun hidePopupWindow() {
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }

    private fun observeLiveData() {
        shareDataViewModel.isShowTvPause.observe(viewLifecycleOwner) { isShowPause ->
            if (isShowPause == true) {
                binding.isPause.visibility = View.VISIBLE
                binding.sumSteps.setTextColor(Color.parseColor("#9D9EA1"))
                binding.tvSteps.setTextColor(Color.parseColor("#9D9EA1"))
            } else {
                binding.isPause.visibility = View.GONE
                binding.sumSteps.setTextColor(Color.parseColor("#363753"))
                binding.tvSteps.setTextColor(Color.parseColor("#363753"))
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "previousStep" -> {
                loadData()
            }
        }
    }

    private fun startForegroundService() {
        try {
            val intentService = Intent(requireContext(), StepCountService::class.java)
            ContextCompat.startForegroundService(requireContext(), intentService)
        } catch (e: Exception) {
            // Handle start service exception
            e.printStackTrace()
        }
    }

    private fun stopForegroundService() {
        val serviceIntent = Intent(requireContext(), StepCountService::class.java)
        requireContext().stopService(serviceIntent)
    }

    override fun onDetach() {
        stopForegroundService()
        sensorManager?.unregisterListener(this)
        super.onDetach()
    }

    private fun popBackStackToA() {
        if (!findNavController().popBackStack()) {
//            Call finish on your Activity
            requireActivity().finish()
        }
    }


}