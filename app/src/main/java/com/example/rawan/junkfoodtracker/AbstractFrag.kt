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
        //this inflates out tab layout file.
        val x = inflater.inflate(R.layout.tab_fragment_layout, null)
        // set up stuff.
        val tabLayout : TabLayout = x.findViewById(R.id.tabs)
        val viewPager: ViewPager = x.findViewById(R.id.viewpager)

        // create a new adapter for our pageViewer. This adapters returns child fragments as per the positon of the page Viewer.
        viewPager.adapter=MyAdapter(childFragmentManager)
        // this is a workaround
        tabLayout.post(Runnable {
            //provide the viewPager to TabLayout.
            tabLayout.setupWithViewPager(viewPager)
        })
        //to preload the adjacent tabs. This makes transition smooth.
        viewPager.setOffscreenPageLimit(2)

        return x



    }
    class MyAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){


        //return the fragment with respect to page position.
        override fun getItem( position:Int): Fragment
        {
            return when (position){
                0 ->  HomeFrag()
                else ->CalenderFrag()
            }

        }

        override fun getCount():Int {
            return 2
        }

        //This method returns the title of the tab according to the position.
        override fun getPageTitle( position:Int):CharSequence {
            return  when (position){
                0 -> "Today"
                else-> "Calender"
            }

        }
    }
}
