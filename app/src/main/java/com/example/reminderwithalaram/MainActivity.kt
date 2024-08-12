package com.example.reminderwithalaram

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderwithalaram.database.AlarmDataEntity
import com.example.reminderwithalaram.viewmodel.sendingDataback
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var createRemainder: Button
    lateinit var recyclerView: RecyclerView
    var list = ArrayList<AlarmDataEntity>()
    lateinit var recyclerViewAdaptor: recyclerViewAdaptor
     var totalList1 :List<AlarmDataEntity> = emptyList()
    lateinit var context: Context


    private val channelId = "your_channel_id"
    private val notificationId = 101


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "setAlarm: date" + System.currentTimeMillis())

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        context = this
        sendingDataback.createDatabase(this)
        lifecycleScope.launch {   sendingDataback.getDataFromDB() }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createRemainder = findViewById(R.id.createRemainderBtn)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        sendingDataback.totalList.observe(this,Observer{
            recyclerViewAdaptor = recyclerViewAdaptor(it)
            recyclerView.adapter = recyclerViewAdaptor
            this.totalList1 = it
            Log.d(TAG, "getDataFromDB: ---"+ this.totalList1 .toString())

        })

        createRemainder.setOnClickListener {
            val indent = Intent(this, CreateAlarmActivity::class.java)
            startActivity(indent)

        }

    }




}