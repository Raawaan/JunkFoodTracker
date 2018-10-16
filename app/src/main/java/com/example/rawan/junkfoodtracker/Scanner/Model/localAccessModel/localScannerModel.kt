package com.example.rawan.junkfoodtracker.Scanner.Model.localAccessModel

import com.example.rawan.junkfoodtracker.Response
import com.example.rawan.junkfoodtracker.Room.*
import com.example.rawan.junkfoodtracker.Scanner.Model.remoteAccessModel.Retrofit.API
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by rawan on 10/10/18.
 */
class localScannerModel(private val database: JFTDatabase){
    fun sameUserWithBarcodeAndDate(userId:Int, barcode:Long,selectedDate:Long):Int{
      return  database.upDao().sameUserSameProduct(userId,
              barcode,selectedDate)
    }
    fun updateCounter(userId:Int,barcode:Long,counterPM:Int){
        var counterTobeUpdated = database.upDao().loadToUpdateCounter(userId, barcode)
        counterTobeUpdated += counterPM
        database.upDao().Update(counterTobeUpdated, userId, barcode)
    }
    fun createNewUserProduct(userId:Int,barcode:Long,counterPM:Int,selectedDate:Long){
        val userProductEntry = UserProductEntity(userId, barcode,
                counterPM,selectedDate)
        database.upDao().insertup(userProductEntry)
    }
    fun selectProductBarcodeIfExisted(barcode: Long):Long{
       return database.productDao().selectProductWithBarcode(barcode)
    }
    fun addProduct(product:ProductEntity){
        database.productDao().insertProduct(product)
    }
    fun selectUserWithEmail(email: String): Int {
       return database.userDao().selectUserWithEmail(email)
    }
    fun createUser(userEntity: UserEntity): Long{
        return database.userDao().insertUser(userEntity)
    }
}