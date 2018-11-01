package com.example.rawan.junkfoodtracker.Today.Model

import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableFromCallable
import org.intellij.lang.annotations.Flow
import java.util.*

/**
 * Created by rawan on 11/10/18.
 */
class TodayModel(val fbAuth: FirebaseAuth, private val database: JFTDatabase) {
    fun requestUserNutritionInformation(): Flowable<NutritionInfo> {
        return database.upDao()
                    .selectSummationOfNutInfo(fbAuth.currentUser?.email!!
                            ,DateWithoutTime.todayDateWithoutTime(Date()))
    }

    fun requestUserProductsList(): Flowable<List<BrandNameAndCounter>> {
        return database.upDao().selectProductsOfCurrentUSer(fbAuth.currentUser?.email!!,
                    DateWithoutTime.todayDateWithoutTime(Date()))
    }
}