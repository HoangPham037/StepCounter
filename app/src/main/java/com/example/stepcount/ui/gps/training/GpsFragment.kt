package com.example.stepcount.ui.gps.training

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant
import com.example.stepcount.Constant.ACTION_PAUSE_SERVICE
import com.example.stepcount.Constant.ACTION_START_OR_RESUME_SERVICE
import com.example.stepcount.Constant.MAP_ZOOM
import com.example.stepcount.Constant.POLYLINE_COLOR
import com.example.stepcount.Constant.POLYLINE_WIDTH
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.data.model.HistoryData
import com.example.stepcount.databinding.FragmentGpsBinding
import com.example.stepcount.service.Polyline
import com.example.stepcount.service.TrackingService
import com.example.stepcount.ui.mainfragment.MainFragmentDirections
import com.example.stepcount.utils.TrackingUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.permissionx.guolindev.PermissionX
import java.util.Date
import kotlin.math.min

class GpsFragment : BaseFragment<FragmentGpsBinding>(
    FragmentGpsBinding::inflate
) {

    companion object {
        fun newInstance(): GpsFragment {
            return GpsFragment()
        }
    }

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var map: GoogleMap? = null
    private var curTimeInMillis = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapFragment.onCreate(savedInstanceState)
        binding.mapFragment.getMapAsync {
            map = it
            addAllPolyline()
        }
        subscribeObserver()
    }

    private fun subscribeObserver() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }
        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        }
        TrackingService.timeRunInMillis.observe(viewLifecycleOwner) {
            curTimeInMillis = it
            val formatTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.tvTime.text = formatTime
        }
    }

    override fun initEventOnClick() {
        super.initEventOnClick()
        binding.btnToggleRun.setOnClickListener {
            requestPermission()
            toggleRun()
        }
        binding.btnFinishRun.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }
    }

    private fun toggleRun() {
        if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.btnToggleRun.text = "Stop"
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.isMyLocationEnabled = true
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.builder()
        for (polyline in pathPoints) {
            for (pos in polyline) {
                bounds.include(pos)
            }
        }
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val minMetric = min(width, height)
        val padding = minMetric.div(10)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                padding
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val currentDate = Date()
            val caloriesBurned = ((distanceInMeters / 1000f) * 59).toInt()
            val historyData =
                HistoryData(
                    img = bmp!!,
                    steps = 5000F,
                    date = currentDate,
                    distance = distanceInMeters.toFloat(),
                    calories = caloriesBurned.toFloat(),
                    time = curTimeInMillis
                )
            val actions =
                MainFragmentDirections.actionMainFragmentToHistoryDetailsFragment(historyData)
            findNavController().navigate(actions)
            appViewModel.saveHistory(historyData)
            Toast.makeText(requireContext(), "Run saved successfully", Toast.LENGTH_SHORT).show()
            stopRun()
            binding.tvTime.text = Constant.TIME_DEFAULT
        }
    }

    private fun stopRun() {
        sendCommandToService(Constant.ACTION_STOP_SERVICE)
    }

    private fun addAllPolyline() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        binding.mapFragment.onResume()
    }

    private fun vectorToBitmap(context: Context, vectorDrawableId: Int): Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableId) as VectorDrawable

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return bitmap
    }

    override fun onStart() {
        super.onStart()
        binding.mapFragment.onStart()
    }


    override fun onPause() {
        super.onPause()
        binding.mapFragment.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapFragment.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapFragment.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapFragment.onSaveInstanceState(outState)
    }

    private fun requestPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .onExplainRequestReason { scope, dinedList ->
                scope.showRequestReasonDialog(
                    dinedList,
                    "Core fundamentals are based on these permissions",
                    "Ok",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Toast.makeText(
                        requireContext(),
                        "All permissions are granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}