package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.tab_fragment_layout.*


/**
 * Created by rawan on 10/09/ddd
 */

class AboutFrag:android.support.v4.app.Fragment(){

    companion object {
        fun newInstance():AboutFrag {
            return AboutFrag()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.about_frag, container, false)

    }
}