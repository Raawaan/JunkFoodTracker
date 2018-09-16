package com.example.rawan.roomjft.Room

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by rawan on 12/09/18.
 */
@Entity(tableName="UserProduct",primaryKeys = ["userId","productId"]
        ,foreignKeys =
           arrayOf(ForeignKey(entity = UserEntity::class,
                   parentColumns = arrayOf("id"),
                   childColumns = arrayOf("userId"))
                   ,ForeignKey(entity = ProductEntity::class,
                   parentColumns = arrayOf("pid"),
                   childColumns = arrayOf("productId"))
           ), indices = arrayOf(Index("userId","productId"))
    )
data class UserProductEntity(
                           var userId:Int ,
                           var productId:Int,
                           var counter:Int,
                           var date: Date)