package com.example.rawan.junkfoodtracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.*
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.preference.DialogPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences
import kotlinx.android.synthetic.main.scanner.*
import kotlinx.android.synthetic.main.setting_frag.*
import kotlin.math.absoluteValue

/**
 * Created by rawan on 18/09/18.
 */
class SettingFrag:  android.support.v4.app.Fragment(){
    companion object {
        fun newInstance():SettingFrag {
            return SettingFrag()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.setting_frag, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shredPref=PreferenceManager.getDefaultSharedPreferences(activity)
        tvEnergySetting.text=shredPref.getString(getString(R.string.energy_key),"2000")
        tvSaturatedFatSetting.text=shredPref.getString(getString(R.string.saturated_fat_key),"30")
        tvSugarsSetting.text=shredPref.getString(getString(R.string.sugars_key),"20")
        tvCarboSetting.text=shredPref.getString(getString(R.string.carbohydrates_key),"38")
                val editor :SharedPreferences.Editor=shredPref.edit()
        saveSetting.setOnClickListener {
    if (!etEnergy.text.isNullOrEmpty())
                editor.putString(getString(R.string.energy_key),etEnergy.text.toString())
    if (!etSaturatedFat.text.isNullOrEmpty())
                editor.putString((getString(R.string.saturated_fat_key)),etSaturatedFat.text.toString())
    if (!etSugars.text.isNullOrEmpty())
                editor.putString(getString(R.string.sugars_key),etSugars.text.toString())
    if (!etCarbohydrates.text.isNullOrEmpty())
                editor.putString(getString(R.string.carbohydrates_key),etCarbohydrates.text.toString())

                editor.apply()
    tvEnergySetting.text=shredPref.getString(getString(R.string.energy_key),"2000")
    tvSaturatedFatSetting.text=shredPref.getString(getString(R.string.saturated_fat_key),"30")
    tvSugarsSetting.text=shredPref.getString(getString(R.string.sugars_key),"20")
    tvCarboSetting.text=shredPref.getString(getString(R.string.carbohydrates_key),"38")
    Toast.makeText(activity, "Thank You", Toast.LENGTH_SHORT).show()
        }
    }
}