package com.example.stepcount.ui.profile.update_profile

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.stepcount.Constant
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.data.model.User
import com.example.stepcount.databinding.FragmentRecommendBinding
import com.permissionx.guolindev.PermissionX

class RecommendFragment : BaseFragment<FragmentRecommendBinding>(
    FragmentRecommendBinding::inflate
) {
    private lateinit var viewPagerAdapter: ViewPagerRecommend
    companion object {
        const val GENDER = "Male"
        const val STEP_LENGTH = 150.0
        const val HEIGHT = 160
        const val WEIGHT = 60.0
        const val GOAL = 5000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User(GENDER, STEP_LENGTH, HEIGHT, WEIGHT, GOAL)
        appViewModel.saveUser(user)
        requestPermission()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerRecommend(childFragmentManager, lifecycle)
        binding.viewpagerManagers.apply {
            offscreenPageLimit = 4
            adapter = viewPagerAdapter
            isUserInputEnabled = false
        }
        appViewModel.registeredUserId.observe(viewLifecycleOwner) {userId->
            userId?.let {
                Log.d("777777", "Recommend: $userId")
            }
        }

//        val userId = MyApplication.loadData(Constant.KEY_USER_ID, Constant.VALUE_DEFAULT)
//        Log.d("777777", "Recommend: $userId")
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!PermissionX.isGranted(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)){
                PermissionX.init(this)
                    .permissions(Manifest.permission.ACTIVITY_RECOGNITION)
                    .onExplainRequestReason { scope, denieList ->
                        scope.showRequestReasonDialog(
                            denieList,
                            "Core fundamental are based on these permissions",
                            "OK",
                            "Cancel"
                        )
                    }
                    .onForwardToSettings { scope, denidList ->
                        scope.showForwardToSettingsDialog(
                            denidList,
                            "You need to allow necessary permissions in Settings manually",
                            "OK",
                            "Cancel"
                        )
                    }
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            Toast.makeText(requireContext(), "All permissions are granted", Toast.LENGTH_LONG)
                                .show()
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

        if (!PermissionX.isGranted(requireContext(), Manifest.permission.BODY_SENSORS)) {
            PermissionX.init(this)
                .permissions(Manifest.permission.BODY_SENSORS,)
                .onExplainRequestReason { scope, denieList ->
                    scope.showRequestReasonDialog(
                        denieList,
                        "Core fundamental are based on these permissions",
                        "OK",
                        "Cancel"
                    )
                }
                .onForwardToSettings { scope, denidList ->
                    scope.showForwardToSettingsDialog(
                        denidList,
                        "You need to allow necessary permissions in Settings manually",
                        "OK",
                        "Cancel"
                    )
                }
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        Toast.makeText(requireContext(), "All permissions are granted", Toast.LENGTH_LONG)
                            .show()
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
}