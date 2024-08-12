package com.example.reminderwithalaram

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.reminderwithalaram.database.AlarmDataEntity
import com.example.reminderwithalaram.utilities.DatePickerFragment
import com.example.reminderwithalaram.utilities.TimePickerFragment
import com.example.reminderwithalaram.utilities.alarmBroadcastReceiver
import com.example.reminderwithalaram.utilities.alarmData
import com.example.reminderwithalaram.utilities.getDateAndTime
import com.example.reminderwithalaram.viewmodel.sendingDataback
import kotlinx.coroutines.launch

class CreateAlarmActivity : AppCompatActivity(), getDateAndTime {

    lateinit var setDate: Button
    lateinit var setTime: Button
    lateinit var setAlarm: Button
    lateinit var cancelAlarm: Button
    lateinit var selectedDate: TextView
    lateinit var selectedTime: TextView
    private val TAG = "CreateAlarmActivity"
    var hourOfDay: Int = 0
    var minute = 0
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var remeinderText: String = ""
    lateinit var reminderTxtView:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_alarm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setDate = findViewById(R.id.selectDate)
        setTime = findViewById(R.id.selectTime)
        selectedDate = findViewById(R.id.dataTextView)
        selectedTime = findViewById(R.id.timeTextView)
        setAlarm = findViewById(R.id.setAlarm)
        cancelAlarm = findViewById(R.id.cancelAlarm)
        reminderTxtView = findViewById(R.id.editTextText)



        setDate.setOnClickListener {

            val newFragment = DatePickerFragment()
            newFragment.setInterface(this)
            newFragment.show(supportFragmentManager, "datePicker")
        }
        setTime.setOnClickListener {
            val newFragment = TimePickerFragment()
            newFragment.setInterface(this)
            newFragment.show(supportFragmentManager, "timePicker")
        }
        setAlarm.setOnClickListener {
            setAlarm()
        }
        cancelAlarm.setOnClickListener {
           // cancelAlarm()
        }
        sendingDataback.requestCode.observe(this, Observer {
            cancelAlarm(it)

        })
    }

    @SuppressLint("NewApi")
    private  fun setAlarm() {
        remeinderText = reminderTxtView.text.toString()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        Log.d(TAG, "setAlarm: date" + System.currentTimeMillis())

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, alarmBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Log.d(TAG, "setAlarm: timestamp while setting "+ calendar.timeInMillis)
            Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show()
        } else {
            Log.d(TAG, "setAlarm: permission not granted")
        }
        val alarmData = AlarmDataEntity(0,hourOfDay,minute,0,year,month,day,remeinderText,calendar.timeInMillis,1)

        lifecycleScope.launch {
            sendingDataback.setAlarmData(alarmData)
        }



        val indent = Intent(this, MainActivity::class.java)
        startActivity(indent)

    }

    private fun cancelAlarm(requestCode:Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, alarmBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_LONG).show()
    }


    @SuppressLint("SetTextI18n")
    override fun getTime(hourOfDay: Int, minute: Int) {
        Log.d(TAG, "getTime: $hourOfDay $minute")
        selectedTime.text = "$hourOfDay : $minute"
        this.hourOfDay = hourOfDay
        this.minute = minute

    }

    @SuppressLint("SetTextI18n")
    override fun getDate(year: Int, month: Int, day: Int) {
        Log.d(TAG, "getDate: $month $year $day")
        selectedDate.text = "$month / $year/  $day"
        this.year = year
        this.month = month
        this.day = day

    }

}