package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import java.util.*

/**
 * Created by rawan on 12/09/18.
 */
@Dao
interface UserProductDao{
    @Query("Select * from UserProduct")
    fun loadAllUsersWithProducts(): List<UserProductEntity>

    @Query("Select min(date) from UserProduct where userId=:userId")
    fun minDate(userId:Int): Long

    @Query("Select counter from UserProduct where userId=:userId AND productBarcode=:productBarcode")
    fun loadToUpdateCounter(userId:Int,productBarcode:Long): Int

    @Query("UPDATE UserProduct SET counter=:counter  WHERE userId=:id AND productBarcode=:barcode")
    fun Update(counter:Int,id:Int,barcode:Long)

    @Query("select counter from UserProduct WHERE userId=:id AND productBarcode=:barcode AND date=:dateSelected")
    fun sameUserSameProduct(id:Int,barcode:Long,dateSelected: Long):Int

    @Query("SELECT * FROM product , user ,UserProduct where user.id=UserProduct.userId AND product.barcode=UserProduct.productBarcode")
    fun loadAllData(): List<AllData>

    @Query("SELECT brandName,counter FROM UserProduct,product where UserProduct.userId=:uId AND userProduct.productBarcode=product.barcode AND  UserProduct.date=:date ORDER BY brandName ASC")
    fun selectProductsOfCurrentUSer(uId:Int,date:Long): List<BrandNameAndCounter>

    @Query("SELECT sum(energy*counter) as energy,sum(saturatedFat*counter) as saturatedFat,sum(sugars*counter) as sugars,sum(carbohydrates*counter) as carbohydrates FROM UserProduct,product where userId=:uId AND userProduct.productBarcode=product.barcode AND UserProduct.date=:date")
    fun selectSummationOfNutInfo(uId:Int,date:Long):NutritionInfo

    @Query("SELECT sum(energy*counter) as energy,sum(saturatedFat*counter) as saturatedFat,sum(sugars*counter) as sugars,sum(carbohydrates*counter) as carbohydrates FROM UserProduct,product where userId=:uId" +
            " AND userProduct.productBarcode=product.barcode AND " +
            "UserProduct.date BETWEEN :fromDate AND :toDate")
    fun selectInDateRange(uId:Int,fromDate:Long,toDate:Long):NutritionInfo

    @Insert
    fun insertup(upEntry: UserProductEntity)

}