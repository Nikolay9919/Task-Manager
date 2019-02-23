package com.example.myapplication.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
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
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {


    private var priorities = arrayOf("Low Priority", "Medium Priority", "High Priority")
    lateinit var priority: String
    var priorityGrade: Int = 0
    private var dateTask: String = ""
    private var timeTask: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setSupportActionBar(toolbar)
        val dbHelper = FeedReaderDbHelper(applicationContext)

        val intent: Intent = intent
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        Log.d("taskId", taskId.toString())
        if (isEdit(taskId)) {
            val task: Task = dbHelper.getTask(taskId)
            editTextTitle.setText(task.title)
        }
        initSpinner()
        initButtons()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        return if (isEdit(taskId)) {
            menuInflater.inflate(R.menu.menu, menu)
            true
        } else false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        val dbHelper = FeedReaderDbHelper(applicationContext)
        if (item != null && isEdit(taskId)) {
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

    private fun initButtons() {
        val dbHelper = FeedReaderDbHelper(applicationContext)
        val taskId: Long = intent.getLongExtra("taskId", taskId.toLong())
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val simpleTimeFormat = SimpleDateFormat("HH:mm:SS", Locale.US)
        if (timeTask.isEmpty()) timeTask = dbHelper.getTask(taskId).time
        if (dateTask.isEmpty()) dateTask = dbHelper.getTask(taskId).date
        button_date.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = simpleDateFormat.format(selectedDate.time)
                    dateTask = date
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        button_time.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                    selectedTime.set(Calendar.MINUTE, minute)
                    val time = simpleTimeFormat.format(selectedTime.time)
                    timeTask = time
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true
            )

            timePicker.show()
        }


        fab.setOnClickListener { view ->
            val title = editTextTitle.text.toString()

            if (taskId == -1L) {
                if (emptyValidation()) {
                    Snackbar.make(view, R.string.empty, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    val task = Task(null, title, false, priority, priorityGrade, dateTask, timeTask)


                    if (task.date.isEmpty()) task.date = "0000-00-00"
                    Log.d("add", task.toString())
                    dbHelper.addTask(task)
                    goHome()
                }
            } else {
                val task = Task(taskId, title, false, priority, priorityGrade, dateTask, timeTask)
                dbHelper.updateTask(task)
                goHome()
            }
        }
    }


    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun emptyValidation(): Boolean {
        if (TextUtils.isEmpty("editTextTitle.text") || TextUtils.isEmpty(dateTask) || TextUtils.isEmpty(timeTask))
            return true
        return false
    }

    private fun isEdit(taskId: Long): Boolean {
        return taskId != -1L
    }

}