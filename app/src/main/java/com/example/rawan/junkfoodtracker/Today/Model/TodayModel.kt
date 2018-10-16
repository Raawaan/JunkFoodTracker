package com.example.rawan.junkfoodtracker.Today.Model

import com.example.rawan.junkfoodtracker.AsyncTaskJFT
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.DateWithoutTime
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.google.firebase.auth.FirebaseAuth
import java.util.*

/**
 * Created by rawan on 11/10/18.
 */
class TodayModel(private val fbAuth: FirebaseAuth,private val database: JFTDatabase){
    val id =database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
//    private fun userId():Int{
//        var id = 0
//        AsyncTaskJFT(inBackground = {
//            return@AsyncTaskJFT database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
//        },onSuccess = {
//            id=it
//        }).execute()
//        return id
//    }
    fun requestUserNutritionInformation():NutritionInfo{
            return database.upDao().selectSummationOfNutInfo(id, DateWithoutTime.todayDateWithoutTime(Date()))
    }
    fun requestUserProductsList():List<BrandNameAndCounter>{
        return database.upDao().selectProductsOfCurrentUSer(id, DateWithoutTime.todayDateWithoutTime(Date()))
    }
}