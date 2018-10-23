package com.example.rawan.junkfoodtracker.Today.Model

import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableFromCallable
import java.util.*

/**
 * Created by rawan on 11/10/18.
 */
class TodayModel(private val fbAuth: FirebaseAuth, private val database: JFTDatabase) {
    fun requestUserNutritionInformation(): Observable<NutritionInfo> {
        return Observable.fromCallable{
            database.upDao().selectSummationOfNutInfo(database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!), DateWithoutTime.todayDateWithoutTime(Date()))
         }
    }

    fun requestUserProductsList(): Observable<List<BrandNameAndCounter>> {
        return Observable.fromCallable {
            database.upDao().selectProductsOfCurrentUSer(database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!),
                    DateWithoutTime.todayDateWithoutTime(Date()))
        }

    }
}