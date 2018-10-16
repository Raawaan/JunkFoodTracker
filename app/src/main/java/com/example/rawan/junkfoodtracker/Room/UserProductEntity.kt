package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by rawan on 12/09/18.
 */
@Entity(tableName="UserProduct",primaryKeys = ["id","productBarcode","date"]
        ,foreignKeys =
arrayOf(ForeignKey(entity = UserEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"))
        ,ForeignKey(entity = ProductEntity::class,
        parentColumns = arrayOf("barcode"),
        childColumns = arrayOf("productBarcode"))
), indices = arrayOf(Index("id","productBarcode"))
)
data class UserProductEntity(
        var id:Int ,
        var productBarcode:Long,
        var counter:Int,
        var date: Long)