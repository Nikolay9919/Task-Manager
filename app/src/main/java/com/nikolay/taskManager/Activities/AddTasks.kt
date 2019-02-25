package com.nikolay.taskManager.Activities

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.nikolay.taskManager.Models.Task
import com.nikolay.taskManager.SQLite.FeedReaderDbHelper
import kotlinx.android.synthetic.main.activity_add_tasks.*
import java.text.SimpleDateFormat
import java.util.*

class AddTasks : AppCompatActivity() {
    private var priorities = arrayOf("Low Priority", "Medium Priority", "High Priority")
    lateinit var priority1: String
    var priorityGrade1: Int = 0
    private var dateTask1: String = ""
    private var timeTask1: String = ""
    lateinit var priority2: String
    var priorityGrade2: Int = 0
    private var dateTask2: String = ""
    private var timeTask2: String = ""
    lateinit var priority3: String
    var priorityGrade3: Int = 0
    private var dateTask3: String = ""
    private var timeTask3: String = ""
    private var dbHelper: FeedReaderDbHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nikolay.taskManager.R.layout.activity_add_tasks)
        dbHelper = FeedReaderDbHelper(applicationContext)
        setSupportActionBar(toolbar_2)
        initSpinners()
        initButtons()
    }


    private fun initSpinners() {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_1.adapter = adapter
        spinner_1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority1 = p0!!.getItemAtPosition(p2) as String
                priorityGrade1 = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                priority1 = "Medium Priority"
            }
        }
        spinner_2.setSelection(1)
        spinner_2.adapter = adapter
        spinner_2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority2 = p0!!.getItemAtPosition(p2) as String
                priorityGrade2 = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                priority2 = "Medium Priority"
            }
        }
        spinner_2.setSelection(1)

        spinner_3.adapter = adapter
        spinner_3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority3 = p0!!.getItemAtPosition(p2) as String
                priorityGrade3 = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                priority3 = "Medium Priority"
            }
        }
        spinner_3.setSelection(1)
    }

    private fun initButtons() {
        dbHelper = FeedReaderDbHelper(applicationContext)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val simpleTimeFormat = SimpleDateFormat("HH:mm:SS", Locale.US)
        button_date_1.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = simpleDateFormat.format(selectedDate.time)
                    dateTask1 = date
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        button_time_1.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                    selectedTime.set(Calendar.MINUTE, minute)
                    val time = simpleTimeFormat.format(selectedTime.time)
                    timeTask1 = time
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true
            )

            timePicker.show()
        }


        button_date_2.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = simpleDateFormat.format(selectedDate.time)
                    dateTask2 = date
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        button_time_2.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                    selectedTime.set(Calendar.MINUTE, minute)
                    val time = simpleTimeFormat.format(selectedTime.time)
                    timeTask2 = time
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true
            )

            timePicker.show()
        }

        button_date_3.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = simpleDateFormat.format(selectedDate.time)
                    dateTask3 = date
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        button_time_3.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                    selectedTime.set(Calendar.MINUTE, minute)
                    val time = simpleTimeFormat.format(selectedTime.time)
                    timeTask3 = time
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true
            )

            timePicker.show()
        }


        fab_add_tasks.setOnClickListener { view ->
            val title1 = editTextTitle_1.text.toString()
            val title2 = editTextTitle_2.text.toString()
            val title3 = editTextTitle_3.text.toString()


            if (emptyValidation()) {
                Snackbar.make(view, com.nikolay.taskManager.R.string.empty, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                val task1 = Task(null, title1, false, priority1, priorityGrade1, dateTask1, timeTask1)
                val task2 = Task(null, title2, false, priority2, priorityGrade2, dateTask2, timeTask2)
                val task3 = Task(null, title3, false, priority3, priorityGrade3, dateTask3, timeTask3)

                dbHelper!!.addTask(task1)
                dbHelper!!.addTask(task2)
                dbHelper!!.addTask(task3)
                goHome()
            }

        }
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    } // to Main Activity

    private fun emptyValidation(): Boolean {
        if (TextUtils.isEmpty(editTextTitle_1.text) || TextUtils.isEmpty(dateTask1) || TextUtils.isEmpty(timeTask1) ||
            TextUtils.isEmpty(editTextTitle_2.text) || TextUtils.isEmpty(dateTask2) || TextUtils.isEmpty(timeTask2) ||
            TextUtils.isEmpty(editTextTitle_3.text) || TextUtils.isEmpty(dateTask3) || TextUtils.isEmpty(timeTask3)
        )
            return true
        return false
    } // Check Edit text date and time

    override fun onDestroy() {
        dbHelper!!.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goHome()
    }
}
