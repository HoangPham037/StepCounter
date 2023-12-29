package com.example.stepcount.ui.mainfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.stepcount.Constant.ACTION_OPEN_HOME_FRAGMENT
import com.example.stepcount.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.stepcount.Constant.IndexPage.indexFive
import com.example.stepcount.Constant.IndexPage.indexFour
import com.example.stepcount.Constant.IndexPage.indexOne
import com.example.stepcount.Constant.IndexPage.indexThree
import com.example.stepcount.Constant.IndexPage.indexTwo
import com.example.stepcount.R
import com.example.stepcount.base.BaseFragment
import com.example.stepcount.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainFragment : BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate
) {
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val mOnNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    binding.viewpagerManager.currentItem = indexOne
                }

                R.id.achievementsFragment -> {
                    binding.viewpagerManager.currentItem = indexTwo
                }

                R.id.statisticsFragment -> {
                    binding.viewpagerManager.currentItem = indexThree
                }

                R.id.gpsFragment -> {
                    binding.viewpagerManager.currentItem = indexFour
                }

                R.id.profileFragment -> {
                    binding.viewpagerManager.currentItem = indexFive
                }
            }
            true
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewpagerManager.apply {
            offscreenPageLimit = 5
            adapter = viewPagerAdapter
            isUserInputEnabled = false
        }
        binding.bottomNav.setOnItemSelectedListener(mOnNavigationItemSelectedListener)
        getIntent(activity?.intent)
        binding.viewpagerManager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val pageItem = binding.bottomNav.menu
                when (position) {
                    indexOne -> pageItem.findItem(R.id.homeFragment).isChecked = true
                    indexTwo -> pageItem.findItem(R.id.achievementsFragment).isChecked = true
                    indexThree -> pageItem.findItem(R.id.statisticsFragment).isChecked = true
                    indexFour -> pageItem.findItem(R.id.gpsFragment).isChecked = true
                    indexFive -> pageItem.findItem(R.id.profileFragment).isChecked = true
                }
            }
        })
    }

    private fun getIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_SHOW_TRACKING_FRAGMENT -> {
                binding.viewpagerManager.setCurrentItem(indexFour, false)
                binding.bottomNav.menu.findItem(R.id.gpsFragment).isChecked = true
            }

            ACTION_OPEN_HOME_FRAGMENT -> {
                binding.viewpagerManager.setCurrentItem(indexOne, false)
                binding.bottomNav.menu.findItem(R.id.homeFragment).isChecked = true
            }
        }
    }
}