package com.example.rawan.junkfoodtracker

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.todolist.AppExecutors
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_frag.*
import kotlinx.android.synthetic.main.scanner.*
import java.util.*

/**
 * Created by rawan on 09/09/18.
 */
class HomeFrag : android.support.v4.app.Fragment() {
    val fbAuth = FirebaseAuth.getInstance()
    lateinit var productAdapter: ProductAdapter
    lateinit var listProducts: List<BrandNameAndCounter>

    override fun onStart() {
        super.onStart()

//        val shredPref=PreferenceManager.getDefaultSharedPreferences(activity)
//                val editor :SharedPreferences.Editor=shredPref.edit()
//        editor.putString("energy","2000")
//        editor.apply()
        val JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID, DateWithoutTime.todayDateWithoutTime(Date()))
        updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)

        listProducts = JFTDatabase.upDao().selectProductsOfCurrentUSer(userID, DateWithoutTime.todayDateWithoutTime(Date()))
        myRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        myRVItem.adapter = productAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppExecutors.instance?.diskIO()?.execute {
            val JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
            val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
            listProducts = JFTDatabase.upDao().selectProductsOfCurrentUSer(userID, DateWithoutTime.todayDateWithoutTime(Date()))
            JFTDatabase.upDao().loadAllData()
            myRVItem.layoutManager = LinearLayoutManager(activity)
            productAdapter = ProductAdapter(listProducts)
        }
    }

    fun updateViews(energy: Long, saturatedFat: Long, sugars: Long, carbohydrates: Long) {
        val shredPref=PreferenceManager.getDefaultSharedPreferences(activity)
        fragTvEnergy.text = getString(R.string.energy) + energy.toString() + getString(R.string.energy_unit)
        if (energy > shredPref.getString(getString(R.string.energy_key),"2000").toInt())
            fragTvEnergy.setTextColor(Color.parseColor("#FF0000"))
        fragTvSaturatedFat.text = getString(R.string.saturated_fat) + saturatedFat.toString() + getString(R.string.unit)
        if (saturatedFat > shredPref.getString(getString(R.string.saturated_fat_key),"30").toInt())
            fragTvSaturatedFat.setTextColor(Color.parseColor("#FF0000"))
        fragTvSugars.text = getString(R.string.sugars) + sugars.toString() + getString(R.string.unit)
        if (sugars > shredPref.getString(getString(R.string.sugars_key),"38").toInt())
            fragTvSugars.setTextColor(Color.parseColor("#FF0000"))
        fragTvCarbohydrates.text = getString(R.string.carbohydrates) + carbohydrates.toString() + getString(R.string.unit)
        if (carbohydrates > shredPref.getString(getString(R.string.carbohydrates_key),"20").toInt())
            fragTvCarbohydrates.setTextColor(Color.parseColor("#FF0000"))
    }
}
