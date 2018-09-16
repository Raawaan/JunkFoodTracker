package com.example.rawan.roomjft.Room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by rawan on 12/09/18.
 */

@Entity(tableName="Product")
data class ProductEntity(@PrimaryKey(autoGenerate = true)var pid:Int,
                     var Productname:String,
                     var kcal:Long,
                     var protein:Long,
                     var carb:Long,
                     var fats:Long,
                     var sugars:Long) {
    @Ignore
    constructor( name:String,
                 kcal:Long,
                 protein:Long,
                 carb:Long,
                 fats:Long,
                 sugars:Long): this(0,name,kcal,protein,carb,fats,sugars)
}