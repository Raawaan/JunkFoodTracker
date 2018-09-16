package com.example.rawan.roomjft.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by rawan on 12/09/18.
 */
@Dao
interface ProductDao {
     @Query("Select * from Product ORDER BY pid")
     fun loadAllProduct(): List<ProductEntity>

    @Query("SELECT * FROM product ORDER BY pid DESC LIMIT 1")
    fun loadLastRow():ProductEntity

    @Insert
    fun insertProduct(productEntry: ProductEntity)
}