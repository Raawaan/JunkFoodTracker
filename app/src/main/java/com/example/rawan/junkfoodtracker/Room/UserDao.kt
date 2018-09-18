package com.example.rawan.roomjft.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by rawan on 12/09/18.
 */
@Dao
interface UserDao{
     @Query("Select * from User ORDER BY id")
      fun loadAllUsers(): List<UserEntity>

    @Query("Select * from User where id=:id")
      fun loadUserById(id:Int): List<UserEntity>

    @Query("SELECT * FROM User ORDER BY id DESC LIMIT 1")
    fun loadLastRow():UserEntity

    @Query("select id from user where email=:email")
    fun selectUserWithEmail(email:String):Int

    @Insert
    fun insertUser(userEntry: UserEntity)
}