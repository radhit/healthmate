package com.healthmate.commons.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()
    private var mSavedInstanceState = Bundle()
    private val mTags = HashMap<Int, String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
        this.notifyDataSetChanged()
    }

    fun clearFragment(){
        fragmentList.clear()
        fragmentTitleList.clear()
        this.notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }


    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        mSavedInstanceState = savedInstanceState ?: Bundle()
    }

    fun onSaveInstanceState(outState: Bundle) {
        val iterator = mTags.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            outState.putString(makeTagName(entry.key), entry.value)
        }
    }

    private fun makeTagName(position: Int): String {
        return ViewPagerAdapter::class.java.name + ":" + position
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}