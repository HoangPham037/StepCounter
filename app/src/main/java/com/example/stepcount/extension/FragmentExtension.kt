package com.example.stepcount.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.stepcount.R
import timber.log.Timber

fun changeFragment(fragment: Fragment, supportFragmentManager: FragmentManager) {
    val isFragmentAdded =
        checkFragmentIsAdded(fragment, supportFragmentManager)
    if (!isFragmentAdded) {
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            setReorderingAllowed(true)
            replace(R.id.nav_host_fragment, fragment)
        }
    } else {
        Timber.i("fragment has been added")
    }
}

fun checkFragmentInBackStack(fragmentManager: FragmentManager, fragmentTag: String): Boolean {
    val backStackCount = fragmentManager.backStackEntryCount
    for (index in 0 until backStackCount) {
        val entry = fragmentManager.getBackStackEntryAt(index)
        if (entry.name == fragmentTag) {
            return true
        }
    }
    return false
}

fun changeFragmentAddToBackStacks(fragment: Fragment, supportFragmentManager: FragmentManager, fragmentTag:String) {
    val isFragmentAdded =
        checkFragmentIsAdded(fragment, supportFragmentManager)
    val isFragmentAddedBackStacks = checkFragmentInBackStack(supportFragmentManager, fragmentTag)
    if (!isFragmentAdded && !isFragmentAddedBackStacks) {
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            setReorderingAllowed(true)
            replace(R.id.nav_host_fragment, fragment)
            addToBackStack(fragmentTag)
        }
    } else {
        Timber.i("fragment has been added")
    }
}

fun <T : Fragment> checkFragmentIsAdded(
    fragment: T,
    supportFragmentManager: FragmentManager
): Boolean {
    val fragmentTag = fragment::class.java.simpleName
    val existingFragment = supportFragmentManager.findFragmentByTag(fragmentTag)
    return existingFragment != null && existingFragment.isAdded
}