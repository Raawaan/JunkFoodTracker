package com.example.rawan.roomjft.Room

import android.arch.persistence.room.PrimaryKey

/**
 * Created by rawan on 13/09/18.
 */
data class AllData(var pid:Int,
                   var Productname:String,
                   var kcal:Long,
                   var protein:Long,
                   var carb:Long,
                   var fats:Long,
                   var sugars:Long,
                   var id:Int,
                   var name:String,
                   var email:String,
                   var userId:Int ,
                   var productId:Int,
                   var counter:Int
)