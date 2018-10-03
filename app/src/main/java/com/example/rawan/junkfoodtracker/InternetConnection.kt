package com.example.rawan.junkfoodtracker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by rawan on 01/10/18.
 */
object InternetConnection{

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }
}