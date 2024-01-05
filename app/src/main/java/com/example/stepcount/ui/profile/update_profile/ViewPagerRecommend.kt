package com.example.stepcount.ui.profile.update_profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stepcount.ui.profile.update_profile.recommend.GendersFragment
import com.example.stepcount.ui.profile.update_profile.recommend.HeightsFragment
import com.example.stepcount.ui.profile.update_profile.recommend.WeightsFragment

class ViewPagerRecommend(fra: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fra, lifecycle) {
    private val list: ArrayList<Fragment> = arrayListOf(
        GendersFragment.getInstance(),
        HeightsFragment.getInstance(),
        WeightsFragment.getInstance(),
    )

    override fun getItemCount(): Int = list.size
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}