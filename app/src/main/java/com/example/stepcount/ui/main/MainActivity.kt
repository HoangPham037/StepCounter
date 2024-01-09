package com.example.stepcount.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stepcount.Constant.ACTION_MOVE_TO_MAIN_FRAGMENT
import com.example.stepcount.Constant.ACTION_OPEN_HOME_FRAGMENT
import com.example.stepcount.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.stepcount.Constant.IndexPage.indexFive
import com.example.stepcount.Constant.IndexPage.indexFour
import com.example.stepcount.Constant.IndexPage.indexOne
import com.example.stepcount.Constant.IndexPage.indexThree
import com.example.stepcount.Constant.IndexPage.indexTwo
import com.example.stepcount.R
import com.example.stepcount.databinding.ActivityMainBinding
import com.example.stepcount.extension.changeFragment
import com.example.stepcount.ui.mainfragment.MainFragment
import com.example.stepcount.ui.splash.SplashFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            changeFragment(SplashFragment(), supportFragmentManager)
        } else {
            Timber.i("SplashFragment already exists")
        }
        navigateToFragment(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToFragment(intent)
    }

    private fun navigateToFragment(intent: Intent?) {
        when (intent?.action) {
            ACTION_OPEN_HOME_FRAGMENT, ACTION_SHOW_TRACKING_FRAGMENT -> {
                changeFragment(MainFragment(), supportFragmentManager)
            }
        }
    }

    override fun onBackPressed() {
        val mainFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? MainFragment
        mainFragment?.let {
            when (it.binding.viewpagerManager.currentItem) {
                indexTwo -> {
                    it.binding.viewpagerManager.currentItem = indexOne
                }

                indexThree -> {
                    it.binding.viewpagerManager.currentItem = indexOne
                }

                indexFour -> {
                    it.binding.viewpagerManager.currentItem = indexOne
                }

                indexFive -> {
                    it.binding.viewpagerManager.currentItem = indexOne
                }

                else -> {
                    super.onBackPressed()
                    moveTaskToBack(true)
                    finish()
                }
            }
        }
    }
}