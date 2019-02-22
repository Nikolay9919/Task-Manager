package com.example.myapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.myapplication.Models.Task
import com.example.myapplication.R
import com.example.myapplication.SQLite.FeedReaderDbHelper
import kotlinx.android.synthetic.main.activity_add_task.*

class AddEditTaskActivity : AppCompatActivity() {


    private var priorities = arrayOf("Low Priority", "Medium Priority", "High Priority")
    lateinit var priority: String
    var priorityGrade: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(toolbar)
        val dbHelper = FeedReaderDbHelper(applicationContext)

        val intent: Intent = intent
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        Log.d("taskId", taskId.toString())
        if (taskId != -1L) {
            val task: Task = dbHelper.getTask(taskId)
            editTextTitle.setText(task.title)
        }
        initSpinner()
        fab.setOnClickListener { view ->
            if (emptyValidation()) {
                Snackbar.make(view, R.string.empty, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                if (taskId == -1L) {
                    val title = editTextTitle.text.toString()
                    val task = Task(null, title, false, priority, priorityGrade)
                    Log.d("add", task.toString())
                    dbHelper.addTask(task)
                    goHome()
                } else {
                    val task: Task = dbHelper.getTask(taskId)
                    dbHelper.updateTask(task)
                    goHome()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        return if (taskId != -1L) {
            menuInflater.inflate(R.menu.menu, menu)
            true
        } else false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        val dbHelper = FeedReaderDbHelper(applicationContext)
        if (item != null && taskId != -1L) {
            when (item.itemId) {
                R.id.action_delete -> {
                    dbHelper.deleteTask(taskId)
                    Log.d("delete", taskId.toString())
                    goHome()
                }
            }
        }
        return true
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority = p0!!.getItemAtPosition(p2) as String
                priorityGrade = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                priority = "Medium Priority"
            }
        }
        spinner.setSelection(1)
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun emptyValidation(): Boolean = TextUtils.isEmpty(editTextTitle.text)

}