package com.example.rawan.junkfoodtracker.Today.Model

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
   private val userID = database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
    fun requestUserNutritionInformation(onSuccess:(nutrition:NutritionInfo)->Unit){
             onSuccess( database.upDao().selectSummationOfNutInfo(userID, DateWithoutTime.todayDateWithoutTime(Date())))
    }
    fun requestUserProductsList(onSuccess:(productsList:List<BrandNameAndCounter>)->Unit){
        onSuccess(database.upDao().selectProductsOfCurrentUSer(userID, DateWithoutTime.todayDateWithoutTime(Date())))
    }
}