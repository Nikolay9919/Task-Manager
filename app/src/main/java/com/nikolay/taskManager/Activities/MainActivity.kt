package com.nikolay.taskManager.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.nikolay.taskManager.Adapter.TaskAdapter
import com.nikolay.taskManager.Models.Task
import com.nikolay.taskManager.R
import com.nikolay.taskManager.SQLite.FeedReaderDbHelper
import com.nikolay.taskManager.Service.NotificationIntentService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val taskList = ArrayList<Task>()
    private var dbHelper: FeedReaderDbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        updateList() // get or update list of tasks
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        updateList()
        initButtons()
        val intentService = Intent(this, NotificationIntentService::class.java)
        startService(intentService)

        isEmpty() // check list of tasks (is empty)

    }

    private fun initButtons() {
        fab_add.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            intent.putExtra("taskId", -1L) // task is null
            startActivity(intent)
        }
        fab_multiple_add.setOnClickListener {
            val intent = Intent(this, AddTasks::class.java)
            intent.putExtra("taskId", -1L) // task is null
            startActivity(intent)
        }
    }

    private fun updateList() {
        taskList.clear()
        dbHelper = FeedReaderDbHelper(applicationContext)
        dbHelper!!.getAllTasks()?.let { taskList.addAll(it) }
        taskList.sortByDescending { it.PriorityGrade } // sorting list by priority
        var taskId = 0L
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TaskAdapter(taskList) {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            taskId = it.id!!
            intent.putExtra("taskId", taskId) // put task id to next activity(if task is null, task id is -1L)
            startActivity(intent)

        }


    }

    private fun isEmpty() {
        if (taskList.isEmpty())
            tvNotTask.visibility = View.VISIBLE
        else
            tvNotTask.visibility = View.INVISIBLE
    }


    override fun onDestroy() {
        dbHelper!!.close()
        super.onDestroy()
    }

    override fun onStop() {
        dbHelper!!.close()
        super.onStop()
    }

    override fun onPause() {
        dbHelper!!.close()
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        exitProcess(-1)
    }


}

