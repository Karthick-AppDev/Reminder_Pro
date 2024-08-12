package com.example.reminderwithalaram.utilities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.example.reminderwithalaram.R
import com.example.reminderwithalaram.afterNotifiactionReceived
import com.example.reminderwithalaram.viewmodel.sendingDataback
import kotlin.math.truncate

class alarmBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "alarmBroadcastReceiver"
    lateinit var notificationManager: NotificationManager
    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationChannel: NotificationChannel
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    lateinit var pendingIntent: PendingIntent
   lateinit var reminderText:String
   

    @SuppressLint("RemoteViewLayout")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive:  broadcast received")
        Log.d(TAG, "onReceive: timestamp while ringing " +System.currentTimeMillis())
        sendingDataback.checkTimeStamp(System.currentTimeMillis())
        Toast.makeText(context, "Alarm ringing..", Toast.LENGTH_LONG).show()

        if (context != null) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        } else {
            Log.d(TAG, "onReceive: notification not created due to null context")
        }

        val intent = Intent(context, afterNotifiactionReceived::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent =
                PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent =
                PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        val contentView = RemoteViews(
            context?.packageName ?: "com.example.reminderwithalaram",
            R.layout.activity_after_notifiaction_received
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    .apply { description = "Test notification" }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            if (context != null) {
                notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Notification Title")
                    .setContentText("Notification Content")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setSilent(true)
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d(TAG, "showNotification: permission not granted")
                        return
                    }
                    notify(1234, notificationBuilder.build())
                }
                Log.d(TAG, "onReceive: notification successfully < Build.VERSION_CODES.O")
                sendingDataback.playAlarmMusic(context)


            } else {
                Log.d(TAG, "onReceive: null context while building notification")
            }


        } else {
            if (context != null) {
                notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Notification Title")
                    .setContentText("Notification Content")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d(TAG, "showNotification: permission not granted")
                        return
                    }
                    notify(1234, notificationBuilder.build())
                }
            }
            notificationManager.notify(1234, notificationBuilder.build())
            Log.d(TAG, "onReceive: notified to notification manager")


        }


    }
}



