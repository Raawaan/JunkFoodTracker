package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.rawan.junkfoodtracker.Room.BrandNameAndCounter
import com.example.rawan.junkfoodtracker.Room.NutritionInfo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.intellij.lang.annotations.Flow
import java.util.*

/**
 * Created by rawan on 12/09/18.
 */
@Dao
interface UserProductDao {
//    @Query("Select * from UserProduct")
//    fun loadAllUsersWithProducts(): List<UserProductEntity>

    @Query("Select min(date) from UserProduct,User where UserProduct.id=User.id")
    fun minDate(): Long

    @Query("Select counter from UserProduct where id=:id AND productBarcode=:productBarcode")
    fun loadToUpdateCounter(id: Int, productBarcode: Long): Int

    @Query("UPDATE UserProduct SET counter=:counter  WHERE id=:id AND productBarcode=:barcode")
    fun update(counter: Int, id: Int, barcode: Long)

    @Query("select counter from UserProduct WHERE id=:id AND productBarcode=:barcode AND date=:dateSelected")
    fun sameUserSameProduct(id: Int, barcode: Long, dateSelected: Long): Int

//    @Query("SELECT * FROM product , user ,UserProduct where user.id=UserProduct.id AND product.barcode=UserProduct.productBarcode")
//    fun loadAllData(): List<AllData>

    @Query("SELECT brandName,counter FROM UserProduct,product,User" +
            " where UserProduct.id=User.id AND" +
            " userProduct.productBarcode=product.barcode AND " +
            " User.email=:email AND UserProduct.date=:date ORDER BY brandName ASC")
    fun selectProductsOfCurrentUSer(email:String, date: Long): Flowable<List<BrandNameAndCounter>>

    @Query("SELECT sum(energy*counter) as energy,sum(saturatedFat*counter)" +
            " as saturatedFat,sum(sugars*counter) as sugars," +
            "sum(carbohydrates*counter) as carbohydrates FROM UserProduct,product,User" +
            " where User.email=:email AND UserProduct.id=User.id " +
            " AND userProduct.productBarcode=product.barcode" +
            " AND UserProduct.date=:date")
    fun selectSummationOfNutInfo(email:String,date: Long): Flowable<NutritionInfo>

    @Query("SELECT sum(energy*counter) as energy," +
            "sum(saturatedFat*counter) as saturatedFat," +
            "sum(sugars*counter) as sugars," +
            "sum(carbohydrates*counter) as carbohydrates" +
            " FROM UserProduct,product,User where User.email=:email" +
            " AND UserProduct.id=User.id" +
            " AND userProduct.productBarcode=product.barcode" +
            " AND UserProduct.date BETWEEN :fromDate AND :toDate")
    fun selectInDateRange(email:String,fromDate: Long, toDate: Long): Flowable<NutritionInfo>

    @Insert
    fun insertUp(upEntry: UserProductEntity)

}