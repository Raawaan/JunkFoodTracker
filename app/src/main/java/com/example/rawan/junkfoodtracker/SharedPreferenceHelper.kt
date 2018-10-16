package com.example.rawan.junkfoodtracker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by rawan on 16/10/18.
 */
object SharedPreferenceHelper {
    fun getValuesSharedPref(context: Context):SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
 }
    fun setValuesSharedPref(context: Context,key:String,value:String){
    return  PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key,value).apply()
 }

}