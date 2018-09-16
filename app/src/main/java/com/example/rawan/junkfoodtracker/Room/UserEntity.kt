package com.example.rawan.roomjft.Room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by rawan on 12/09/18.
 */
@Entity(tableName="User")
data class UserEntity(@PrimaryKey(autoGenerate = true)var id:Int,
                     var name:String,
                     var email:String) {
    @Ignore
    constructor(name: String,email: String): this(0,name,email)
}