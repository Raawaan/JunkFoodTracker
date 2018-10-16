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
    val id =database.userDao().selectUserWithEmail(fbAuth.currentUser?.email!!)
//    private fun userId():Int{
//        var id = 0
//    AsyncTaskJFT(inBackground = {
//       return@AsyncTaskJFT
//    },onSuccess = {
//        id=it
//     }).execute()
//            return id
//    }
    fun getMinDate():Long{
      return  database.upDao().minDate(id)
    }
    fun requestUserNutritionInformation(date:Long):NutritionInfo{
       return database.upDao().selectSummationOfNutInfo(id, date)
    }
    fun requestUserProductsList(date:Long):List<BrandNameAndCounter>{
        return database.upDao().selectProductsOfCurrentUSer(id,date)
    }
    fun selectNunInfoOfSelectedPeriod(from:Long, to:Long):NutritionInfo{
      return database.upDao().selectInDateRange(id, from, to)
    }
}