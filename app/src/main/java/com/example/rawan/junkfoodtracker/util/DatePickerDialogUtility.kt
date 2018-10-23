package com.example.rawan.junkfoodtracker.util

import android.app.DatePickerDialog
import android.content.Context
import com.example.rawan.junkfoodtracker.Room.DataConverter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import java.util.*

/**
 * Created by rawan on 16/10/18.
 */
object DatePickerDialogUtility {
    fun create(context: Context ,onPickDate:(date:Date,year:Int,month:Int,day:Int)->Unit) :DatePickerDialog {

        val date = Date()
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDayOfMonth ->
            calender.set(Calendar.YEAR, mYear)
            calender.set(Calendar.MONTH, mMonth)
            calender.set(Calendar.DAY_OF_MONTH, mDayOfMonth)
            val selectedDate = DataConverter().toData(DateWithoutTime.todayDateWithoutTime(calender.time))!!
            onPickDate(selectedDate,mYear,mMonth,mDayOfMonth)
        }, year, month, day)
        datePicker.datePicker.maxDate = date.time
        return datePicker
    }
}