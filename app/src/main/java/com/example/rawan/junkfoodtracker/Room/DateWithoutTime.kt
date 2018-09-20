package com.example.rawan.junkfoodtracker.Room

import java.util.*

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
}