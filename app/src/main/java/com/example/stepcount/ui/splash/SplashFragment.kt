package com.example.stepcount.ui.splash

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.stepcount.Constant.KEY_USER_ID
import com.example.stepcount.Constant.VALUE_USER_ID_DEFAULT
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.databinding.FragmentSplashBinding
import com.example.stepcount.extension.changeFragment
import com.example.stepcount.ui.mainfragment.MainFragment
import com.example.stepcount.ui.profile.update_profile.RecommendFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private lateinit var animationTop: Animation
    private lateinit var animationBottom: Animation
    private val splashViewModel: SplashViewModel by viewModel()
    override fun setupView() {
        super.setupView()
        setAnimation()
    }

    private fun setAnimation() {
        animationTop = AnimationUtils.loadAnimation(requireContext(), R.anim.top)
        animationBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom)
        binding.logo.animation = animationTop
        binding.title.animation = animationBottom
        binding.description.animation = animationBottom
    }

    override fun setupDataObserver() {
        super.setupDataObserver()
        splashViewModel.navigateToNextScreen.observe(viewLifecycleOwner) {
            val userId = MyApplication.loadIntData(KEY_USER_ID, VALUE_USER_ID_DEFAULT)
            if (userId > 0) {
                activity?.let {
                    changeFragment(MainFragment(), it.supportFragmentManager)
                }
            } else {
                activity?.let {
                    changeFragment(RecommendFragment(), it.supportFragmentManager)
                }
            }
        }
        splashViewModel.startDelay()
    }
}