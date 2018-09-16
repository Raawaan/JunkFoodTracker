package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by rawan on 10/09/18.
 */
class ProfileFrag:android.support.v4.app.Fragment(){
    companion object {
        fun newInstance():ProfileFrag {
            return ProfileFrag()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_frag, container, false)
    }
}