package com.example.stepcount.ui.mainfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stepcount.ui.achievements.AchievementsFragment
import com.example.stepcount.ui.gps.training.GpsFragment
import com.example.stepcount.ui.home.HomeFragment
import com.example.stepcount.ui.profile.ProfileFragment
import com.example.stepcount.ui.statistics.StatisticsFragment

class ViewPagerAdapter(fra: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fra, lifecycle) {
    private val list: ArrayList<Fragment> = arrayListOf(
        HomeFragment.newInstance(),
        AchievementsFragment.newInstance(),
        StatisticsFragment.newInstance(),
        GpsFragment.newInstance(),
        ProfileFragment.newInstance()
    )

    override fun getItemCount(): Int = list.size
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}