package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by rawan on 12/09/18.
 */

@Entity(tableName="Product")
data class ProductEntity(@PrimaryKey(autoGenerate = true)var barcode:Long,
                     var brandName:String,
                     var energy:Long,
                     var saturatedFat:Long,
                     var sugars:Long,
                     var carbohydrates:Long)