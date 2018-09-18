package com.example.rawan.roomjft.Room

import android.arch.persistence.room.PrimaryKey

/**
 * Created by rawan on 13/09/18.
 */
data class AllData(var barcode:Long,
                   var brandName:String,
                   var energy:Long,
                   var saturatedFat:Long,
                   var sugars:Long,
                   var carbohydrates:Long,
                   var id:Int,
                   var name:String,
                   var email:String,
                   var userId:Int ,
                   var productBarcode:Int,
                   var counter:Int
)