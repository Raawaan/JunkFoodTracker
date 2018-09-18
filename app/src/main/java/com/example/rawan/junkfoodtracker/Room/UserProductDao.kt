package com.example.rawan.roomjft.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by rawan on 12/09/18.
 */
@Dao
interface UserProductDao{
    @Query("Select * from UserProduct")
    fun loadAllUsersWithProducts(): List<UserProductEntity>

    @Query("Select counter from UserProduct where userId=:userId AND productBarcode=:productBarcode")
    fun loadToUpdateCounter(userId:Int,productBarcode:Long): Int

    @Query("UPDATE UserProduct SET counter=:counter  WHERE userId=:id AND productBarcode=:barcode")
    fun Update(counter:Int,id:Int,barcode:Long)

    @Query("select counter from UserProduct WHERE userId=:id AND productBarcode=:barcode")
    fun sameUserSameProduct(id:Int,barcode:Long):Int

    @Query("SELECT * FROM product , user ,UserProduct where product.barcode=UserProduct.userId AND user.id=UserProduct.productBarcode")
    fun loadAllData(): List<AllData>

    @Insert
    fun insertup(upEntry: UserProductEntity)

}