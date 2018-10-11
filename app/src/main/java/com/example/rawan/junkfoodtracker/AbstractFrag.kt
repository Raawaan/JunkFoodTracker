package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.View.X
import android.view.ViewGroup

/**
 * Created by rawan on 19/09/18.
 */
class AbstractFrag:android.support.v4.app.Fragment(){
    companion object {
        fun newInstance():AbstractFrag {
            return AbstractFrag()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val x = inflater.inflate(R.layout.tab_fragment_layout, null)
        val tabLayout : TabLayout = x.findViewById(R.id.tabs)
        val viewPager: ViewPager = x.findViewById(R.id.viewpager)
        viewPager.adapter=MyAdapter(childFragmentManager)
        tabLayout.post({
            tabLayout.setupWithViewPager(viewPager)
        })
        viewPager.offscreenPageLimit=2
        return x
    }
    class MyAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){
        override fun getItem( position:Int): Fragment
        {
            return when (position){
                0 -> HomeFrag()
                else ->CalenderFrag()
            }
        }
        override fun getCount():Int {
            return 2
        }
        override fun getPageTitle( position:Int):CharSequence {
            return  when (position){
                0 -> "Today"
                else-> "Calender"
            }
        }
    }
}
