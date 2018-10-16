package com.example.rawan.junkfoodtracker

import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.setting_frag.*

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

        tvEnergySetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.energy_key),"2000")
        tvSaturatedFatSetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.saturated_fat_key),"30")
        tvSugarsSetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.sugars_key),"20")
        tvCarboSetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.carbohydrates_key),"38")

        saveSetting.setOnClickListener {
            if (!etEnergy.text.isNullOrEmpty())
                SharedPreferenceHelper.setValuesSharedPref(activity!!.applicationContext,getString(R.string.energy_key),etEnergy.text.toString())
            if (!etSaturatedFat.text.isNullOrEmpty())
                SharedPreferenceHelper.setValuesSharedPref(activity!!.applicationContext,getString(R.string.saturated_fat_key),etSaturatedFat.text.toString())
            if (!etSugars.text.isNullOrEmpty())
                SharedPreferenceHelper.setValuesSharedPref(activity!!.applicationContext,getString(R.string.sugars_key),etSugars.text.toString())
            if (!etCarbohydrates.text.isNullOrEmpty())
                SharedPreferenceHelper.setValuesSharedPref(activity!!.applicationContext,getString(R.string.carbohydrates_key),etCarbohydrates.text.toString())

            tvEnergySetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.energy_key),"2000")
            tvSaturatedFatSetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.saturated_fat_key),"30")
            tvSugarsSetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.sugars_key),"20")
            tvCarboSetting.text=SharedPreferenceHelper.getValuesSharedPref(activity!!.applicationContext).getString(getString(R.string.carbohydrates_key),"38")

            Toast.makeText(activity, getString(R.string.thank_you), Toast.LENGTH_SHORT).show()
        }
    }
}