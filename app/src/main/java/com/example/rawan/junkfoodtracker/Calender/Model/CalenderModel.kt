package com.example.rawan.junkfoodtracker.Calender.Model

import com.example.rawan.junkfoodtracker.AsyncTaskJFT
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.JFTDatabase
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by rawan on 11/10/18.
 */
class CalenderModel(private val fbAuth: FirebaseAuth, private val database: JFTDatabase){
    fun getMinDate():Long{
      return  database.upDao().minDate(database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!))
    }
    fun requestUserNutritionInformation(date:Long):NutritionInfo{
       return database.upDao().selectSummationOfNutInfo(database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!), date)
    }
    fun requestUserProductsList(date:Long):List<BrandNameAndCounter>{
        return database.upDao().selectProductsOfCurrentUSer(database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!),date)
    }
    fun selectNunInfoOfSelectedPeriod(from:Long, to:Long):NutritionInfo{
      return database.upDao().selectInDateRange(database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!), from, to)
    }
}