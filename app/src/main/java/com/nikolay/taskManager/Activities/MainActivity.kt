package com.nikolay.taskManager.Activities

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nikolay.taskManager.Adapter.TaskAdapter
import com.nikolay.taskManager.Fragments.AddTaskFragment
import com.nikolay.taskManager.Fragments.AddTasksFragment
import com.nikolay.taskManager.Models.Task
import com.nikolay.taskManager.R
import com.nikolay.taskManager.SQLite.FeedReaderDbHelper
import com.nikolay.taskManager.Service.JobService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val taskList = ArrayList<Task>()
    private var dbHelper: FeedReaderDbHelper? = null
    private val manager = supportFragmentManager

    @RequiresApi(Build.VERSION_CODES.O)
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
        isEmpty() // check list of tasks (is empty)

    }

    override fun onDestroy() {
        startService() // start Notification Service
        dbHelper!!.close()
        super.onDestroy()
    }

    override fun onStop() {
        startService() // start Notification Service
        dbHelper!!.close()
        super.onStop()
    }

    override fun onPause() {
        startService() // start Notification Service
        dbHelper!!.close()
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        updateList()
        initButtons()
    }

    private fun initButtons() {
        fab_add.setOnClickListener {
            val bundle = Bundle()
            val transaction = manager.beginTransaction()
            val fragment = AddTaskFragment()
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            transaction.add(R.id.fragment_holder, fragment)
            bundle.putLong("taskId", -1L)
            fragment.arguments = bundle
            transaction.addToBackStack(null)
            transaction.commit()
            fab_add.visibility = View.GONE
            fab_multiple_add.visibility = View.GONE

        }
        fab_multiple_add.setOnClickListener {
            val transaction = manager.beginTransaction()
            val fragment = AddTasksFragment()
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
            transaction.add(R.id.fragment_holder, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
            fab_add.visibility = View.GONE
            fab_multiple_add.visibility = View.GONE

        }
    }

    private fun updateList() {
        fab_add.visibility = View.VISIBLE
        fab_multiple_add.visibility = View.VISIBLE
        taskList.clear()
        dbHelper = FeedReaderDbHelper(applicationContext)
        dbHelper!!.getAllTasks()?.let { taskList.addAll(it) }
        taskList.sortByDescending { it.PriorityGrade } // sorting list by priority
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TaskAdapter(taskList) {
            val bundle = Bundle()
            val transaction = manager.beginTransaction()
            val fragment = AddTaskFragment()
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            transaction.replace(R.id.fragment_holder, fragment)
            bundle.putLong("taskId", it.id!!)
            fragment.arguments = bundle
            transaction.addToBackStack(null)
            transaction.commit()
            fab_add.visibility = View.GONE
            fab_multiple_add.visibility = View.GONE

        }

    }

    private fun isEmpty() {
        if (taskList.isEmpty())
            tvNotTask.visibility = View.VISIBLE
        else
            tvNotTask.visibility = View.INVISIBLE
    }

    private fun startService() {
        val componentName = ComponentName(this, JobService::class.java)
        val jobInfo = JobInfo.Builder(123, componentName)
            .setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(15 * 60 * 1000)
            .build()
        val scheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(jobInfo)


    }


}

