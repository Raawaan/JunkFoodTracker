package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by rawan on 12/09/18.
 */
@Entity(tableName="UserProduct",primaryKeys = ["userId","productBarcode","date"]
        ,foreignKeys =
arrayOf(ForeignKey(entity = UserEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"))
        ,ForeignKey(entity = ProductEntity::class,
        parentColumns = arrayOf("barcode"),
        childColumns = arrayOf("productBarcode"))
), indices = arrayOf(Index("userId","productBarcode"))
)
data class UserProductEntity(
        var userId:Int ,
        var productBarcode:Long,
        var counter:Int,
        var date: Long)