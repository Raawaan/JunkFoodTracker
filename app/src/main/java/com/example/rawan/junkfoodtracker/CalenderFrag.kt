package com.example.rawan.junkfoodtracker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rawan.junkfoodtracker.R.drawable.date
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.example.rawan.roomjft.Room.DataConverter
import com.example.rawan.roomjft.Room.JFTDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.bottom_fragment.*
import kotlinx.android.synthetic.main.bottom_fragment.view.*
import kotlinx.android.synthetic.main.calender_frag.*
import kotlinx.android.synthetic.main.home_frag.*
import java.util.*
import kotlin.math.log


/**
 * Created by rawan on 19/09/18.
 */

class CalenderFrag : android.support.v4.app.Fragment(){
    private val fbAuth = FirebaseAuth.getInstance()
    private var calender: Calendar = Calendar.getInstance()
    private val date = Date()
    lateinit var productAdapter: ProductAdapter
    lateinit var listProducts: List<BrandNameAndCounter>
    lateinit var JFTDatabase: JFTDatabase
    lateinit var toDate:Date
    lateinit var fromDate:Date

    override fun onStart() {
        super.onStart()
        //in OnStart to update views onRestart
        JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        val yesterday = DateWithoutTime.todayDateWithoutTime(calender.time)
        calendarView.date = yesterday
        listProducts = JFTDatabase.upDao().selectProductsOfCurrentUSer(userID, yesterday)
        calMyRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        calMyRVItem.adapter = productAdapter
        val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID, yesterday)
        updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calender_frag, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBottomSheetBehavior()
        JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        //update calender default data to yesterday
        calender.add(Calendar.DATE, -1)
        val yesterday = DateWithoutTime.todayDateWithoutTime(calender.time)
        val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID, yesterday)
        updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            month.plus(1)
            calender?.set(year, month, dayOfMonth)
            val selectedDateWithoutTime = DateWithoutTime.todayDateWithoutTime(calender.time)
            val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID, selectedDateWithoutTime)
            calProductListTitle.text = getString(R.string.productTitle) + dayOfMonth + "/" + month.plus(1) + "/" + year
            calTotalNutiTitle.text = getString(R.string.nutTitle) + dayOfMonth + "/" + month.plus(1) + "/" + year
            updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)
            listProducts = JFTDatabase.upDao().selectProductsOfCurrentUSer(userID, selectedDateWithoutTime)
            calMyRVItem.layoutManager = LinearLayoutManager(activity)
            productAdapter = ProductAdapter(listProducts)
            calMyRVItem.adapter = productAdapter
        }
        bottomSheetLogic(userID)
    }
    private fun bottomSheetLogic(userID: Int) {
        val calender = Calendar.getInstance()
        var year = calender.get(Calendar.YEAR)
        var month = calender.get(Calendar.MONTH)
        var day = calender.get(Calendar.DAY_OF_MONTH)
        fromBtn.setOnClickListener {
            val fromPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDayOfMonth ->
                tvFrom.text = " " + mDayOfMonth + "/" + mMonth + "/" + mYear
                calender.set(Calendar.YEAR, mYear)
                calender.set(Calendar.MONTH, mMonth)
                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                fromDate = calender.time
            }, year, month, day)
            fromPicker.show()
        }
        toBtn.setOnClickListener {
            val toPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDayOfMonth ->
                tvTo.text = " " + mDayOfMonth + "/" + mMonth + "/" + mYear
                calender.set(Calendar.YEAR, mYear)
                calender.set(Calendar.MONTH, mMonth)
                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                toDate = calender.time

            }, year, month, day)
            toPicker.show()
            confrimDateBtn.visibility = View.VISIBLE
        }
        confrimDateBtn.setOnClickListener {
            toDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(toDate))!!
            fromDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(fromDate))!!
            if (fromDate.after(toDate) || toDate.after(DataConverter().toData(DateWithoutTime.todayDateWithoutTime(date)))) {
                Toast.makeText(activity, "Invaild Period", Toast.LENGTH_SHORT).show()
            } else {
                val fromDateLong = DateWithoutTime.todayDateWithoutTime(fromDate)
                val toDateLong = DateWithoutTime.todayDateWithoutTime(toDate)
                val nutInfoInDateRange = JFTDatabase.upDao().selectInDateRange(userID, fromDateLong, toDateLong)
                nutInfoRange.visibility = View.VISIBLE
                sheetFragTvEnergy.text = getString(R.string.energy) + nutInfoInDateRange.energy.toString() + getString(R.string.energy_unit)
                sheetFragTvSaturatedFat.text = getString(R.string.saturated_fat) + nutInfoInDateRange.saturatedFat.toString() + getString(R.string.unit)
                sheetFragTvSugars.text = getString(R.string.sugars) + nutInfoInDateRange.sugars.toString() + getString(R.string.unit)
                sheetFragTvCarbohydrates.text = getString(R.string.carbohydrates) + nutInfoInDateRange.carbohydrates.toString() + getString(R.string.unit)
            }
        }
    }
    private fun handleBottomSheetBehavior() {
        val behavior = BottomSheetBehavior.from(bottomSheetLayout)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetHeading.setBackgroundResource(R.drawable.arrow)
                    BottomSheetBehavior.STATE_EXPANDED -> bottomSheetHeading.setBackgroundResource(R.drawable.down_arrow)
                }

            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val s = "Sliding..."
            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun updateViews(energy: Long, saturatedFat: Long, sugars: Long, carbohydrates: Long) {
        calFragTvEnergy.text = getString(R.string.energy) + energy.toString() + getString(R.string.energy_unit)
        calFragTvSaturatedFat.text = getString(R.string.saturated_fat) + saturatedFat.toString() + getString(R.string.unit)
        calFragTvSugars.text = getString(R.string.sugars) + sugars.toString() + getString(R.string.unit)
        calFragTvCarbohydrates.text = getString(R.string.carbohydrates) + carbohydrates.toString() + getString(R.string.unit)
    }
}