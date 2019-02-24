package com.example.myapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.myapplication.Models.Task
import com.example.myapplication.R
import com.example.myapplication.SQLite.FeedReaderDbHelper
import com.example.myapplication.Service.NotificationIntentService
import com.example.myapplication.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {


    private val taskList = ArrayList<Task>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val intent = Intent(this, AddEditTaskActivity::class.java)
        val intentService = Intent(this, NotificationIntentService::class.java)
        startService(intentService)
        updateList()
        isEmpty()


        fab_add.setOnClickListener {
            intent.putExtra("taskId", -1L)
            startActivity(intent)
        }

    }


    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun updateList() {
        taskList.clear()
        val dbHelper = FeedReaderDbHelper(applicationContext)
        dbHelper.getAllTasks()?.let { taskList.addAll(it) }
        taskList.sortByDescending { it.PriorityGrade }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TaskAdapter(taskList) {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            Log.d("tap", it.id.toString())
            intent.putExtra("taskId", it.id)
            startActivity(intent)
        }

    }

    private fun isEmpty() {
        if (taskList.isEmpty())
            tvNotTask.visibility = View.VISIBLE
        else
            tvNotTask.visibility = View.INVISIBLE
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val intentService = Intent(this, NotificationIntentService::class.java)
        startService(intentService)
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onDestroy() {
        val intentService = Intent(this, NotificationIntentService::class.java)
        startService(intentService)
        super.onDestroy()
    }

    override fun onStop() {
        val intentService = Intent(this, NotificationIntentService::class.java)
        startService(intentService)
        super.onStop()
    }

    override fun onPause() {
        val intentService = Intent(this, NotificationIntentService::class.java)
        startService(intentService)
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        exitProcess(-1)
    }
}

