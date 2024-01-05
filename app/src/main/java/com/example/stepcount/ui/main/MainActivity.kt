package com.example.stepcount.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.stepcount.Constant.ACTION_OPEN_HOME_FRAGMENT
import com.example.stepcount.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.stepcount.R
import com.example.stepcount.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var active = false
    override fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val window: Window = this.window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = resources.getColor(R.color.white, null)
//        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateToGPSFragment(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToGPSFragment(intent)
    }

    override fun onStart() {
        super.onStart()
        active = true
    }

    override fun onStop() {
        super.onStop()
        active = false
    }
    private fun navigateToGPSFragment(intent: Intent?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        when (intent?.action) {
            ACTION_OPEN_HOME_FRAGMENT -> {
                navController.navigate(R.id.action_global_homeFragment)
            }
            ACTION_SHOW_TRACKING_FRAGMENT -> {
                navController.navigate(R.id.action_global_homeFragment)
            }
        }
    }
}