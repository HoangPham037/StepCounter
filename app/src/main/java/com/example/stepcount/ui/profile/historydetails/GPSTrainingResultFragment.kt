package com.example.stepcount.ui.profile.historydetails

import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentHistoryDetailsBinding
import com.example.stepcount.utils.TrackingUtility


class GPSTrainingResultFragment : BaseFragment<FragmentHistoryDetailsBinding>(
    FragmentHistoryDetailsBinding::inflate
) {
    private val args:GPSTrainingResultFragmentArgs  by navArgs()
    private var id: Long? = null
    override fun setupView() {
        super.setupView()
        val history = args.historyData
        val step = history.steps
        val time = TrackingUtility.getFormattedStopWatchTime(history.time)
        val distance = history.distance
        val kcal = history.calories
        val img = history.img
        binding.apply {
            tvTime.text = time
            tvSumKcal.text = kcal.toString()
            tvSumKm.text = distance.toString()
            tvSumSteps.text = step.toString()
            Glide.with(requireContext()).load(img).into(imgHistoryMap)
        }
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.imgBackHistoryDetails.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imgDeleteHistory.setOnClickListener {
            showDialogAlertConfirm()
        }
    }

    private fun showDialogAlertConfirm() {
        val dialogShowAlert = AlertDialog.Builder(requireContext())
        dialogShowAlert.setMessage("Are you sure you want to delete?")
        dialogShowAlert.setPositiveButton("Yes") { _, _ ->
            val step = args.historyData
            id = step.id
            appViewModel.deleteHistoryById(id!!)
            findNavController().navigateUp()
        }
        dialogShowAlert.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        dialogShowAlert.create().show()
    }
}