package com.example.rawan.junkfoodtracker.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

/**
 * Created by rawan on 12/09/18.
 */
@Database(entities = [(UserEntity::class),(ProductEntity::class),(UserProductEntity::class)], version =1, exportSchema = false)
@TypeConverters(DataConverter::class)
    abstract class  JFTDatabase : RoomDatabase() {
    abstract fun userDao():UserDao
    abstract fun upDao():UserProductDao
    abstract fun productDao():ProductDao

    companion object {
        var DATABASE_NAME = "JFTDatabase"
        private var INSTANCE: JFTDatabase? = null
        @Synchronized
        fun getInstance(context:
                        Context): JFTDatabase {
            if (INSTANCE == null)
            {
                INSTANCE = Room
                        .databaseBuilder(context.applicationContext,
                                JFTDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE!!
        }

    }
}