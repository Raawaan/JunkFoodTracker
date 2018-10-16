package com.example.rawan.junkfoodtracker.Calender.View

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rawan.junkfoodtracker.Calender.Model.CalenderModel
import com.example.rawan.junkfoodtracker.Calender.Presenter.CalenderPresenter
import com.example.rawan.junkfoodtracker.Calender.Presenter.CalenderPresenterImp
import com.example.rawan.junkfoodtracker.ProductAdapter
import com.example.rawan.junkfoodtracker.R
import com.example.rawan.junkfoodtracker.Room.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.bottom_fragment.*
import kotlinx.android.synthetic.main.calender_frag.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by rawan on 19/09/18.
 */

class CalenderFragment : Fragment(),CalenderView {
    private var calender: Calendar = Calendar.getInstance()
    private val date = Date()
    private lateinit var fromPicker :DatePickerDialog
    private lateinit var toPicker :DatePickerDialog
    private lateinit var calenderPresenter: CalenderPresenter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var listProducts: List<BrandNameAndCounter>
    private lateinit var toDate:Date
    private lateinit var fromDate:Date
    override fun onStart() {
        super.onStart()
        //set min and max date
        calenderPresenter=CalenderPresenterImp(CalenderModel(FirebaseAuth.getInstance(),com.example.rawan.junkfoodtracker.Room.JFTDatabase.getInstance(activity!!.applicationContext)),this)
        calenderPresenter.selectMinDate()
        calendarView.maxDate=date.time

        //update views to yesterday's date format
        val myDate = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        val yesterday= DateWithoutTime.todayDateWithoutTime(myDate)
        val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH)
        calProductListTitle.text=getString(R.string.productTitle) +dateFormat.format(yesterday)
        calTotalNutiTitle.text=getString(R.string.nutTitle) + dateFormat.format(yesterday)
        calendarView.date = yesterday

        calenderPresenter.requestCurrentUserNutritionInfo(yesterday)
        calenderPresenter.requestCurrentUserProductsList(yesterday)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calender_frag, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBottomSheetBehavior()
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            month.plus(1)
            calProductListTitle.text = getString(R.string.productTitle) + dayOfMonth + "/" + month.plus(1) + "/" + year
            calTotalNutiTitle.text = getString(R.string.nutTitle) + dayOfMonth + "/" + month.plus(1) + "/" + year

            calender.set(year, month, dayOfMonth)
            val selectedDateWithoutTime= DateWithoutTime.todayDateWithoutTime(calender.time)
            calenderPresenter.requestCurrentUserNutritionInfo(selectedDateWithoutTime)
            calenderPresenter.requestCurrentUserProductsList(selectedDateWithoutTime)
     }
        bottomSheetLogic()
    }
    private fun bottomSheetLogic() {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        fromBtn.setOnClickListener {
             fromPicker= DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
                tvFrom.text = " " + mDayOfMonth + "/" + mMonth.plus(1) + "/" + mYear
                calender.set(Calendar.YEAR, mYear)
                calender.set(Calendar.MONTH, mMonth)
                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                fromDate = calender.time
            }, year, month, day)
            calenderPresenter.selectMinDateFromPicker()
            fromPicker.datePicker.maxDate=date.time
            fromPicker.show()
        }
        toBtn.setOnClickListener {
                toPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
                tvTo.text = " " + mDayOfMonth + "/" + mMonth.plus(1) + "/" + mYear
                calender.set(Calendar.YEAR, mYear)
                calender.set(Calendar.MONTH, mMonth)
                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
                toDate = calender.time

            }, year, month, day)
            calenderPresenter.selectMinDateToPicker()
            toPicker.datePicker.maxDate=date.time
            toPicker.show()
            confrimDateBtn.visibility = View.VISIBLE
        }
        confrimDateBtn.setOnClickListener {
            toDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(toDate))!!
            fromDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(fromDate))!!
            if (fromDate.after(toDate) || toDate.after(DataConverter().toData(DateWithoutTime.todayDateWithoutTime(date)))) {
                Toast.makeText(activity, getString(R.string.warning_msg), Toast.LENGTH_SHORT).show()
            } else {
                calenderPresenter.selectNutritionInfoOfSelectedPeriod(DateWithoutTime.todayDateWithoutTime(fromDate),
                        DateWithoutTime.todayDateWithoutTime(toDate))
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
            }
        })
    }
    override fun acceptMinDate(minDate: Long) {
        if(minDate!=0.toLong()&&minDate<DateWithoutTime.todayDateWithoutTime(date)){
            calendarView.minDate = minDate
        }
        else
            calendarView.minDate=minDate
    }
    override fun acceptMinDateFromPicker(minDate: Long) {
        fromPicker.datePicker.minDate=minDate
    }
    override fun acceptMinDateToPicker(minDate: Long) {
        toPicker.datePicker.minDate=minDate
    }
    override fun acceptNutritionInfo(nutrition: NutritionInfo) {
        calFragTvEnergy.text = getString(R.string.energy) + nutrition.energy.toString() + getString(R.string.energy_unit)
        calFragTvSaturatedFat.text = getString(R.string.saturated_fat) + nutrition.saturatedFat.toString() + getString(R.string.unit)
        calFragTvSugars.text = getString(R.string.sugars) + nutrition.sugars.toString() + getString(R.string.unit)
        calFragTvCarbohydrates.text = getString(R.string.carbohydrates) + nutrition.carbohydrates.toString() + getString(R.string.unit)
    }
    override fun acceptUsersProductList(productList: List<BrandNameAndCounter>) {
        listProducts =productList
        calMyRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        calMyRVItem.adapter = productAdapter
    }
    override fun acceptNutInfoOfSelectedPeriod(nutrition: NutritionInfo) {
        nutInfoRange.visibility = View.VISIBLE
        sheetFragTvEnergy.text = getString(R.string.energy) + nutrition.energy.toString() + getString(R.string.energy_unit)
        sheetFragTvSaturatedFat.text = getString(R.string.saturated_fat) + nutrition.saturatedFat.toString() + getString(R.string.unit)
        sheetFragTvSugars.text = getString(R.string.sugars) + nutrition.sugars.toString() + getString(R.string.unit)
        sheetFragTvCarbohydrates.text = getString(R.string.carbohydrates) + nutrition.carbohydrates.toString() + getString(R.string.unit)
    }
}