package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.TypeConverter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import kotlinx.android.synthetic.main.calender_frag.*
import java.util.*



/**
 * Created by rawan on 30/08/18.
 */
class DataConverter{
    @TypeConverter
    fun toData(timestamp:Long?):Date? {
        return if (timestamp==null)
            null
        else
            Date(timestamp)
    }
//    @TypeConverter
//    fun toTimestamp(date: Date):Long?{
//        return if (date==null)
//            null
//        else
//        {
//            val date = Date()
//            DateWithoutTime.todayDateWithoutTime(date)
//        }
//    }
}