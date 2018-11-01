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
import com.example.rawan.junkfoodtracker.util.DatePickerDialogUtility
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.bottom_fragment.*
import kotlinx.android.synthetic.main.calender_frag.*
import kotlinx.android.synthetic.main.scanner.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by rawan on 19/09/18.
 */

class CalenderFragment : Fragment(), CalenderView {
    val firebaseAuth=FirebaseAuth.getInstance()
    private var calender: Calendar = Calendar.getInstance()
    private val date = Date()
    private lateinit var fromPicker: DatePickerDialog
    private lateinit var toPicker: DatePickerDialog
    private lateinit var calenderPresenter: CalenderPresenter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var listProducts: List<BrandNameAndCounter>
    private var toDate= Date()
    private var fromDate= Date()
    override fun onStart() {
        super.onStart()
        //set min and max date
        calenderPresenter = CalenderPresenterImp(CalenderModel(firebaseAuth,
                com.example.rawan.junkfoodtracker.Room.JFTDatabase.getInstance(activity!!.applicationContext)), this)
        calenderPresenter.selectMinDate()
        calendarView.maxDate = date.time

        //update views to yesterday's date format
        val myDate = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        val yesterday = DateWithoutTime.todayDateWithoutTime(myDate)
        val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH)
        calProductListTitle.text = getString(R.string.productTitle, dateFormat.format(yesterday))
        calTotalNutiTitle.text = getString(R.string.nutTitle, dateFormat.format(yesterday))
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
            calProductListTitle.text = getString(R.string.productTitle, getString(R.string.date_format, dayOfMonth.toString(), month.plus(1).toString()
                    , year.toString()))
            calTotalNutiTitle.text = getString(R.string.nutTitle, getString(R.string.date_format, dayOfMonth.toString(), month.plus(1).toString(), year.toString()))

            calender.set(year, month, dayOfMonth)
            val selectedDateWithoutTime = DateWithoutTime.todayDateWithoutTime(calender.time)
            calenderPresenter.requestCurrentUserNutritionInfo(selectedDateWithoutTime)
            calenderPresenter.requestCurrentUserProductsList(selectedDateWithoutTime)
        }
        bottomSheetLogic()
    }

    private fun bottomSheetLogic() {
        fromBtn.setOnClickListener {
            fromPicker = DatePickerDialogUtility.create(context = context!!, onPickDate = { date, year, month, day ->
                fromDate = date
                tvFrom.text = getString(R.string.date_format, day.toString(), month.plus(1).toString()
                        , year.toString())
            })
            calenderPresenter.selectMinDateFromPicker()
        }
        toBtn.setOnClickListener {
            toPicker = DatePickerDialogUtility.create(context = context!!, onPickDate = { date, year, month, day ->
                toDate = date
                tvTo.text = getString(R.string.date_format, day.toString(), month.plus(1).toString()
                        , year.toString())
            })
            calenderPresenter.selectMinDateToPicker()
//            toPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
//                tvTo.text = getString(R.string.date_format, mDayOfMonth.toString(), mMonth.toString()
//                        , mYear.toString())
//                calender.set(Calendar.YEAR, mYear)
//                calender.set(Calendar.MONTH, mMonth)
//                calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
//                toDate = calender.time
//
//            }, year, month, day)
//            calenderPresenter.selectMinDateToPicker()
//            toPicker.datePicker.maxDate = date.time
//            toPicker.show()
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
        val myDate = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        val yesterday = DateWithoutTime.todayDateWithoutTime(myDate)
        if (minDate != 0.toLong() && minDate <= DateWithoutTime.todayDateWithoutTime(date)) {
            calendarView.minDate = minDate
        } else if (minDate == 0.toLong())
            calendarView.minDate = yesterday
    }

    override fun acceptMinDateFromPicker(minDate: Long) {
        fromPicker.datePicker.minDate = minDate
        fromPicker.show()
    }

    override fun acceptMinDateToPicker(minDate: Long) {
        toPicker.datePicker.minDate = minDate
        toPicker.show()
    }

    override fun acceptNutritionInfo(nutrition: NutritionInfo) {
        calFragTvEnergy.text = getString(R.string.energy, nutrition.energy.toString(), getString(R.string.energy_unit))
        calFragTvSaturatedFat.text = getString(R.string.saturated_fat, nutrition.saturatedFat.toString(), getString(R.string.unit))
        calFragTvSugars.text = getString(R.string.sugars, nutrition.sugars.toString(), getString(R.string.unit))
        calFragTvCarbohydrates.text = getString(R.string.carbohydrates, nutrition.carbohydrates.toString(), getString(R.string.unit))
    }

    override fun acceptUsersProductList(productList: List<BrandNameAndCounter>) {
        listProducts = productList
        calMyRVItem.layoutManager = LinearLayoutManager(activity)
        productAdapter = ProductAdapter(listProducts)
        calMyRVItem.adapter = productAdapter
    }

    override fun acceptNutInfoOfSelectedPeriod(nutrition: NutritionInfo) {
        nutInfoRange.visibility = View.VISIBLE
        sheetFragTvEnergy.text = getString(R.string.energy, nutrition.energy.toString(), getString(R.string.energy_unit))
        sheetFragTvSaturatedFat.text = getString(R.string.saturated_fat, nutrition.saturatedFat.toString(), getString(R.string.unit))
        sheetFragTvSugars.text = getString(R.string.sugars, nutrition.sugars.toString(), getString(R.string.unit))
        sheetFragTvCarbohydrates.text = getString(R.string.carbohydrates, nutrition.carbohydrates.toString(), getString(R.string.unit))
    }
    override fun exception(e: Throwable) {
        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
    }
    override fun onDestroy() {
        calenderPresenter.onDetach()
        super.onDestroy()
    }
}