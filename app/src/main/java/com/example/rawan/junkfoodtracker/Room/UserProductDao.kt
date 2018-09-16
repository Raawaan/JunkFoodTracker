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

    @Query("SELECT * FROM product , user ,UserProduct where product.pid=UserProduct.userId AND user.id=UserProduct.productId")
    fun loadAllData(): List<AllData>

    @Insert
    fun insertup(upEntry: UserProductEntity)

}