package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by rawan on 18/09/18.
 */
class SettingFrag:android.support.v4.app.Fragment(){
    companion object {
        fun newInstance():SettingFrag {
            return SettingFrag()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.setting_frag, container, false)
    }
}