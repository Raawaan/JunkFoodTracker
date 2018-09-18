package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rawan.junkfoodtracker.Retrofit.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by rawan on 09/09/18.
 */
class HomeFrag:android.support.v4.app.Fragment(){
    companion object {
        fun newInstance():HomeFrag {
            return HomeFrag()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag, container, false)
    }

}
