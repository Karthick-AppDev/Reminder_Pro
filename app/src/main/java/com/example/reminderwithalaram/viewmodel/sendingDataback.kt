package com.example.reminderwithalaram.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.reminderwithalaram.database.AlarmDao
import com.example.reminderwithalaram.database.AlarmDataEntity
import com.example.reminderwithalaram.database.DatabaseAlarm
import com.example.reminderwithalaram.utilities.alarmBroadcastReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object sendingDataback {
    private const val TAG = "sendingDataback"

    var alarmData = MutableLiveData<AlarmDataEntity>()
    var totalList =  MutableLiveData<List<AlarmDataEntity>>()
    lateinit var database: DatabaseAlarm
    lateinit var alarmDao: AlarmDao
    val executorService: ExecutorService = Executors.newSingleThreadExecutor() // Creates a pool of 4 threads
    var currentAlarmList =  MutableLiveData<List<AlarmDataEntity>>()
    lateinit var ringPlayer:MediaPlayer
    var requestCode = MutableLiveData<Int>()


    suspend fun setAlarmData(alarmData: AlarmDataEntity) {
        this.alarmData.value = alarmData
        alarmDao.setAlarmDataToTable(alarmData)
       totalList.value = alarmDao.getAlarmDataFromTable()
    }



    suspend fun getDataFromDB() {
        totalList.value = alarmDao.getAlarmDataFromTable()
        Log.d(TAG, "getDataFromDB: "+ totalList.toString())
    }

    fun createDatabase(context: Context) {
        database = DatabaseAlarm.getDatabase(context)
        alarmDao = database.alarmDao()

    }

    fun playAlarmMusic(context:Context){
         ringPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
        ringPlayer.start()
    }
    fun stopAlarmMusic(){
        ringPlayer.stop()
    }


     fun checkTimeStamp(currentTimestamp: Long){
        val timestamp = currentTimestamp // Example timestamp
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())

        val hours = localDateTime.hour
        val minutes = localDateTime.minute

         executorService.execute {
             val currentAlarmList1 : List<AlarmDataEntity> = alarmDao.getCurrentAlarmRow(hours,minutes)


             Handler(Looper.getMainLooper()).post{
                 currentAlarmList.value = currentAlarmList1
                 Log.d(TAG, "timestamp while : Hours: $hours, Minutes: $minutes list:$currentAlarmList")
             }

         }
         Log.d(TAG, "timestamp while : Hours: $hours, Minutes: $minutes list:$currentAlarmList")


         // Log.d(TAG, "timestamp while : Hours: $hours, Minutes: $minutes list:$list")


    }



}