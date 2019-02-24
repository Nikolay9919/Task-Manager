package com.example.myapplication.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.example.myapplication.Models.Task
import com.example.myapplication.R
import com.example.myapplication.SQLite.FeedReaderDbHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class NotificationIntentService : IntentService("Notification") {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.myapplication"
    private val description = "Test Notification"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(p0: Intent?) {
        checkDateTime(getList())
    }

    private fun startNotify(contentTitle: String, contentText: String) {
        val intent = Intent(this, LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId)
                .setContentTitle(contentTitle).setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                .setContentTitle(contentTitle).setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
        }
        for (i in 1..1) {
            notificationManager.notify(1234, builder.build())
        }
    }


    private fun getList(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        val dbHelper = FeedReaderDbHelper(applicationContext)
        dbHelper.getAllTasks()?.let { taskList.addAll(it) }
        return taskList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDateTime(taskList: ArrayList<Task>) {

        val simpleInputTimeFormat = SimpleDateFormat("HH:mm:SS", Locale.US)
        val simpleOutputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)
        val timeNowInput = simpleInputTimeFormat.parse(LocalTime.now().toString())
        val timeNow = simpleOutputTimeFormat.format(timeNowInput)
        for (i in taskList) {
            val inputTime = simpleInputTimeFormat.parse(i.time)
            val time = simpleOutputTimeFormat.format(inputTime)
            if (i.date.contains(LocalDate.now().toString())) {

                if (time.contains(timeNow)) {
                    startNotify(i.title, i.time)
                    Log.d("service", i.toString())
                    Thread.sleep(30000)
                    checkDateTime(taskList)
                }
            }

        }
        Log.d("service1", taskList.toString())
        Thread.sleep(10000)
        checkDateTime(getList())
    }
}