package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by rawan on 12/09/18.
 */
@Dao
interface ProductDao {
     @Query("Select * from Product ORDER BY barcode")
     fun loadAllProduct(): List<ProductEntity>

    @Query("select barcode from Product where barcode=:barcode")
    fun selectProductWithBarcode(barcode:Long):Long

    @Query("SELECT * FROM product ORDER BY barcode DESC LIMIT 1")
    fun loadLastRow():ProductEntity

    @Insert
    fun insertProduct(productEntry: ProductEntity)
}