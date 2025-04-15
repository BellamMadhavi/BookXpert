package com.example.bookxpert.roomdb

import android.content.Context
import androidx.room.*

@Database(entities = [ObjectEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun objectDao(): ObjectDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "object_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}