package com.nikolay.taskManager.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.RemoteViews
import com.nikolay.taskManager.Activities.MainActivity
import com.nikolay.taskManager.Models.Task
import com.nikolay.taskManager.R
import com.nikolay.taskManager.SQLite.FeedReaderDbHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class JobService : JobService() {

    var dbHelper: FeedReaderDbHelper? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("Jobstarted", "Started")
        checkDateTime(getList(), params)
        return true
    }


    override fun onStopJob(params: JobParameters?): Boolean {
        dbHelper!!.close()
        return true

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkDateTime(taskList: ArrayList<Task>, jobParameters: JobParameters?) {
        val simpleInputTimeFormat = SimpleDateFormat("HH:mm:SS", Locale.US)
        val simpleOutputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)
        val timeNowInput = simpleInputTimeFormat.parse(LocalTime.now().toString())
        val timeNow = simpleOutputTimeFormat.format(timeNowInput)
        Log.d("service", "service")

        Log.d("service1", "service1")
        for (i in taskList) {
            Log.d("service2", "service2")
            val inputTime = simpleInputTimeFormat.parse(i.time)
            val time = simpleOutputTimeFormat.format(inputTime)
            if (i.date.contains(LocalDate.now().toString())) {

                if (time.contains(timeNow)) {
                    startNotify(i.title, time, i.Priority)
                    Log.d("service3", i.toString())


                }
                Log.d("service1", taskList.toString())
            }
        }
        jobFinished(jobParameters, true)

    }

    private fun getList(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        dbHelper = FeedReaderDbHelper(applicationContext)
        dbHelper!!.getAllTasks()?.let { taskList.addAll(it) }
        return taskList
    }

    private fun startNotify(contentTitle: String, contentText: String, contentPriority: String) {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId = "com.example.myapplication"
        val description = "Test Notification"
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title, contentTitle)
        contentView.setTextViewText(R.id.tv_content, contentText)
        contentView.setTextViewText(R.id.tv_priority, contentPriority)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
        }
        for (i in 1..1) {
            notificationManager.notify(1234, builder.build())
        }
    }
}