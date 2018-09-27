package com.example.rawan.junkfoodtracker.Room

import android.util.Log
import java.util.*
import java.sql.Timestamp







/**
 * Created by rawan on 20/09/18.
 */
object DateWithoutTime{
    fun todayDateWithoutTime(date:Date):Long{
    val calendar = Calendar.getInstance()
    calendar.time =date
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time.time
    }

    fun startOfDay(time: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time
        cal.set(Calendar.HOUR_OF_DAY, 0) //set hours to zero
        cal.set(Calendar.MINUTE, 0) // set minutes to zero
        cal.set(Calendar.SECOND, 0) //set seconds to zero
        return cal.timeInMillis / 1000
    }
}