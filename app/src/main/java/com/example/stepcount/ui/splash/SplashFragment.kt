package com.example.stepcount.ui.splash

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.findNavController
import com.example.stepcount.Constant.KEY_USER_ID
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {
    private lateinit var animationTop: Animation
    private lateinit var animationBottom: Animation
    private val splashViewModel: SplashViewModel by viewModel()
    override fun setupView() {
        super.setupView()
        setAnimation()
    }
    private fun setAnimation(){
        animationTop = AnimationUtils.loadAnimation(requireContext(), R.anim.top)
        animationBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom)
        binding.logo.animation = animationTop
        binding.title.animation = animationBottom
        binding.description.animation = animationBottom
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        splashViewModel.navigateToNextScreen.observe(viewLifecycleOwner) {
            appViewModel.registeredUserId.observe(viewLifecycleOwner) { userId ->
                if (userId != null) {
                    navigateToHomeScreen()
                } else {
                    navigateToRecommendScreen()
                }
            }
        }
        splashViewModel.startDelay()
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }

    private fun navigateToRecommendScreen() {
        findNavController().navigate(R.id.action_splashFragment_to_recommendFragment)
    }
}