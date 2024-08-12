package com.example.reminderwithalaram.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao {
    @Insert
     suspend fun setAlarmDataToTable(AlarmDataEntity: AlarmDataEntity)

    @Query("SELECT * FROM alarmDataTable")
      suspend fun getAlarmDataFromTable():List<AlarmDataEntity>
      @Query("SELECT * FROM ALARMDATATABLE WHERE hourOfDay = :hours AND minute = :minute")
       fun getCurrentAlarmRow(hours:Int,minute:Int):List<AlarmDataEntity>
}