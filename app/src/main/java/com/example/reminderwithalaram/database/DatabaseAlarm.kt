package com.example.reminderwithalaram.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmDataEntity::class], version = 1)
abstract class DatabaseAlarm :RoomDatabase(){
    abstract fun alarmDao():AlarmDao
    companion object {
        @Volatile
        private var INSTANCE: DatabaseAlarm? = null

        fun getDatabase(context: Context): DatabaseAlarm {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseAlarm::class.java,
                    "DatabaseAlarm"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}