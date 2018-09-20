package com.example.rawan.junkfoodtracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.example.rawan.roomjft.Room.DataConverter
import com.example.rawan.roomjft.Room.JFTDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.calender_frag.*
import kotlinx.android.synthetic.main.home_frag.*
import java.util.*


/**
 * Created by rawan on 19/09/18.
 */

class CalenderFrag:android.support.v4.app.Fragment(){
    val fbAuth= FirebaseAuth.getInstance()
    var calender:Calendar= Calendar.getInstance()
    lateinit var productAdapter: ProductAdapter
    lateinit var listProducts: List<String>
    lateinit var JFTDatabase:JFTDatabase
    companion object {
        fun newInstance():CalenderFrag {
            return CalenderFrag()
        }
    }

    override fun onStart() {
        super.onStart()
        JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
//        calender.add(Calendar.DATE,-1)
        val yesterday = DateWithoutTime.todayDateWithoutTime(calender.time)
        calendarView.date=yesterday
        listProducts = JFTDatabase.upDao().selectProductsOfCurrentUSer(userID,yesterday)
        calMyRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        calMyRVItem.adapter = productAdapter
        val nutrition=JFTDatabase.upDao().selectSummationOfNutInfo(userID,yesterday)
        updateViews(nutrition.energy,nutrition.saturatedFat,nutrition.sugars,nutrition.carbohydrates)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calender_frag, container, false)
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        calender.add(Calendar.DATE,-1)
        val yesterday = DateWithoutTime.todayDateWithoutTime(calender.time)
        val nutrition=JFTDatabase.upDao().selectSummationOfNutInfo(userID,yesterday)
        updateViews(nutrition.energy,nutrition.saturatedFat,nutrition.sugars,nutrition.carbohydrates)

        calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            month.plus(1)
            calender?.set(year, month, dayOfMonth)
            val selectedDateWithoutTime = DateWithoutTime.todayDateWithoutTime(calender.time)
            val nutrition=JFTDatabase.upDao().selectSummationOfNutInfo(userID,selectedDateWithoutTime)
            calProductListTitle.text="Products of $dayOfMonth/${month.plus(1)}/$year"
            calTotalNutiTitle.text="Total Nutirtion Info of $dayOfMonth/${month.plus(1)}/$year"
            updateViews(nutrition.energy,nutrition.saturatedFat,nutrition.sugars,nutrition.carbohydrates)
            listProducts =JFTDatabase.upDao().selectProductsOfCurrentUSer(userID,selectedDateWithoutTime)
            calMyRVItem.layoutManager = LinearLayoutManager(activity)
            productAdapter = ProductAdapter(listProducts)
            calMyRVItem.adapter = productAdapter
        }

    }
    @SuppressLint("SetTextI18n")
   private fun updateViews(energy :Long, saturatedFat:Long, sugars:Long, carbohydrates:Long){
        calFragTvEnergy.text=getString(R.string.energy)+energy.toString()+getString(R.string.energy_unit)
        calFragTvSaturatedFat.text=getString(R.string.saturated_fat)+saturatedFat.toString() +getString(R.string.unit)
        calFragTvSugars.text=getString(R.string.sugars)+sugars.toString()+getString(R.string.unit)
        calFragTvCarbohydrates.text=getString(R.string.carbohydrates)+carbohydrates.toString() +getString(R.string.unit)
    }
}