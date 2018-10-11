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
import android.widget.CalendarView
import android.widget.Toast
import com.example.android.todolist.AppExecutors
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log




/**
 * Created by rawan on 19/09/18.
 */

class CalenderFrag : android.support.v4.app.Fragment(){
    private val fbAuth = FirebaseAuth.getInstance()
    private var calender: Calendar = Calendar.getInstance()
    private val date = Date()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var listProducts: List<BrandNameAndCounter>
    private lateinit var JFTDatabase: JFTDatabase
    private lateinit var toDate:Date
    private lateinit var fromDate:Date
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onStart() {
        super.onStart()
        //in OnStart to update views onRestart


            JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
            val userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        val mydate = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        val yesterday = DateWithoutTime.todayDateWithoutTime(mydate)
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        calProductListTitle.text=getString(R.string.productTitle) +dateFormat.format(yesterday)
        calTotalNutiTitle.text=getString(R.string.nutTitle) + dateFormat.format(yesterday)
            val isDateExist= JFTDatabase.upDao().minDate(userID)
        if(isDateExist!=0.toLong()&&isDateExist<DateWithoutTime.todayDateWithoutTime(date)){
            calendarView.minDate = isDateExist
        }
        else
            calendarView.minDate=yesterday

            calendarView.maxDate=date.time

        val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID, yesterday)
            updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)
            calendarView.date = yesterday
            listProducts = JFTDatabase.upDao().selectProductsOfCurrentUSer(userID, yesterday)
            calMyRVItem.layoutManager = LinearLayoutManager(activity)
            productAdapter = ProductAdapter(listProducts)
            calMyRVItem.adapter = productAdapter
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calender_frag, container, false)
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        handleBottomSheetBehavior()
        JFTDatabase = com.example.rawan.roomjft.Room.JFTDatabase.getInstance(activity!!.applicationContext)
        var userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
        //update calender default data to yesterday
        val mydate = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        val yesterday = DateWithoutTime.todayDateWithoutTime(mydate)
        val nutrition = JFTDatabase.upDao().selectSummationOfNutInfo(userID, yesterday)
        updateViews(nutrition.energy, nutrition.saturatedFat, nutrition.sugars, nutrition.carbohydrates)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            month.plus(1)
            calender.set(year, month, dayOfMonth)
            userID = JFTDatabase.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
            val selectedDateWithoutTime= DateWithoutTime.todayDateWithoutTime(calender.time)
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
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        fromBtn.setOnClickListener {
            val fromPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDayOfMonth ->
                tvFrom.append( "" + mDayOfMonth + "/" + mMonth.plus(1) + "/" + mYear)
                calender.set(Calendar.YEAR, mYear)
                calender.set(Calendar.MONTH, mMonth)
                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                fromDate = calender.time
            }, year, month, day)
            fromPicker.datePicker.minDate=JFTDatabase.upDao().minDate(userID)
            fromPicker.datePicker.maxDate=date.time
            fromPicker.show()
        }
        toBtn.setOnClickListener {
            val toPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
                tvTo.append(" " + mDayOfMonth + "/" + mMonth.plus(1) + "/" + mYear)
                calender.set(Calendar.YEAR, mYear)
                calender.set(Calendar.MONTH, mMonth)
                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                toDate = calender.time

            }, year, month, day)
            toPicker.datePicker.minDate=JFTDatabase.upDao().minDate(userID)
            toPicker.datePicker.maxDate=date.time
            toPicker.show()
            confrimDateBtn.visibility = View.VISIBLE
        }
        confrimDateBtn.setOnClickListener {
            toDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(toDate))!!
            fromDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(fromDate))!!
            if (fromDate.after(toDate) || toDate.after(DataConverter().toData(DateWithoutTime.todayDateWithoutTime(date)))) {
                Toast.makeText(activity, getString(R.string.invalid_period), Toast.LENGTH_SHORT).show()
            } else {
                val fromDateLong = DateWithoutTime.todayDateWithoutTime(fromDate)
                val toDateLong = DateWithoutTime.todayDateWithoutTime(toDate)
                val nutInfoInDateRange = JFTDatabase.upDao().selectInDateRange(userID, fromDateLong, toDateLong)
                nutInfoRange.visibility = View.VISIBLE
                sheetFragTvEnergy.append(nutInfoInDateRange.energy.toString() + getString(R.string.energy_unit))
                sheetFragTvSaturatedFat.append(nutInfoInDateRange.saturatedFat.toString() + getString(R.string.unit))
                sheetFragTvSugars.append(nutInfoInDateRange.sugars.toString() + getString(R.string.unit))
                sheetFragTvCarbohydrates.append(nutInfoInDateRange.carbohydrates.toString() + getString(R.string.unit))
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
    private fun updateViews(energy: Long, saturatedFat: Long, sugars: Long, carbohydrates: Long) {
        calFragTvEnergy.append(energy.toString() + getString(R.string.energy_unit))
        calFragTvSaturatedFat.append(saturatedFat.toString() + getString(R.string.unit))
        calFragTvSugars.append(sugars.toString() + getString(R.string.unit))
        calFragTvCarbohydrates.append(carbohydrates.toString() + getString(R.string.unit))
    }

}