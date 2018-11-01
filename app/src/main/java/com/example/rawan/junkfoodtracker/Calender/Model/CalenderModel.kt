package com.example.rawan.junkfoodtracker.Calender.Model

import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.intellij.lang.annotations.Flow
import java.util.*

/**
 * Created by rawan on 11/10/18.
 */
class CalenderModel(val fbAuth: FirebaseAuth,private val database: JFTDatabase) {
    fun getMinDate(): Single<Long> {
        var date =   Single.fromCallable {
            val f = DateWithoutTime.todayDateWithoutTime(Date())
                    if (database.upDao().minDate() == 0.toLong())
                        f
                    else
                        database.upDao().minDate()
        }
        return date

    }
    fun requestUserNutritionInformation(date: Long): Flowable<NutritionInfo> {
        return  database.upDao().selectSummationOfNutInfo(fbAuth.currentUser?.email!!,date)

    }
    fun requestUserProductsList(date: Long): Flowable<List<BrandNameAndCounter>> {
        return  database.upDao().selectProductsOfCurrentUSer(fbAuth.currentUser?.email!!,
                date)
    }
    fun selectNunInfoOfSelectedPeriod(from: Long, to: Long): Flowable<NutritionInfo> {
        return database.upDao().selectInDateRange(fbAuth.currentUser?.email!!,
                from, to)
    }
}