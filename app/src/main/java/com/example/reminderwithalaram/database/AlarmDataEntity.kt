package com.example.reminderwithalaram.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "alarmDataTable")
data class AlarmDataEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    var hourOfDay: Int,
    var minute: Int,
    var second:Int,
    var year: Int,
    var month: Int,
    var day: Int,
    var reminderText:String,
    var timestamp: Long,
    var requestCode:Int
)