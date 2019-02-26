package com.nikolay.taskManager.Fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.nikolay.taskManager.Models.Task
import com.nikolay.taskManager.R
import com.nikolay.taskManager.SQLite.FeedReaderDbHelper
import kotlinx.android.synthetic.main.activity_add_task.*
import java.text.SimpleDateFormat
import java.util.*

class AddTaskFragment : Fragment() {

    private var priorities = arrayOf("Low Priority", "Medium Priority", "High Priority")
    lateinit var priority: String
    var priorityGrade: Int = 0
    private var dateTask: String = ""
    private var timeTask: String = ""
    private var taskId: Long = 0
    private var dbHelper: FeedReaderDbHelper? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttach(context: Context?) {
        super.onAttach(context)
         taskId = arguments!!.getLong("taskId", taskId)
        Log.d("taskIdFragment", arguments!!.getLong("taskId", taskId).toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dbHelper = FeedReaderDbHelper(activity!!.applicationContext)

        return inflater.inflate(R.layout.activity_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initButtons(taskId)
    }

    override fun onStart() {
        super.onStart()
        if (isEdit(taskId)) { // if task id = -1L, activity start for add task
            val task: Task = dbHelper!!.getTask(taskId)
            editTextTitle.setText(task.title)
            initTV(task.title, task.Priority, task.date + task.time)
        }
    }

    override fun onDestroy() {
        dbHelper!!.close()
        super.onDestroy()
    }

    private fun initTV(title: String, priority: String, time: String) {
        tv_title.text = title
        tv_priority.text = priority
        tv_time.text = time

    }


    private fun initSpinner() {
        val adapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, priorities)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtons(taskId: Long) {
        dbHelper = FeedReaderDbHelper(activity!!.applicationContext)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val simpleTimeFormat = SimpleDateFormat("HH:mm:SS", Locale.US)
        if (timeTask.isEmpty()) timeTask = dbHelper!!.getTask(taskId).time
        if (dateTask.isEmpty()) dateTask = dbHelper!!.getTask(taskId).date
        button_date.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                activity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = simpleDateFormat.format(selectedDate.time)
                    dateTask = date
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = now.timeInMillis
            datePicker.show()
        }

        button_time.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                activity, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
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


        fab_add_fragment.setOnClickListener { view ->
            val title = editTextTitle.text.toString()

            if (taskId == -1L) {
                if (emptyValidation()) {
                    Snackbar.make(view, R.string.empty, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    val task = Task(null, title, false, priority, priorityGrade, dateTask, timeTask)
                    Log.d("taskIdAdd", task.toString())
                    dbHelper!!.addTask(task)

                }
            } else {
                val task = Task(taskId, title, false, priority, priorityGrade, dateTask, timeTask)
                Log.d("taskIdUpdate", task.toString())
                dbHelper!!.updateTask(task)
                activity!!.onBackPressed()
            }
        }

        Log.d("taskidininit", taskId.toString())
        if (taskId == -1L)
            fab_delete.visibility = View.INVISIBLE
        else {
            fab_delete.setOnClickListener {
                val dialog = AlertDialog.Builder(activity!!.applicationContext)
                dialog.setTitle(R.string.are_sure)
                dialog.setMessage(R.string.delete_task)
                dialog.setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                    dbHelper!!.deleteTask(taskId)
                    activity!!.onBackPressed()
                }
                dialog.setNegativeButton(R.string.no) { _: DialogInterface, _: Int ->
                }

                dialog.show()
            }
        }


    }

    private fun emptyValidation(): Boolean {
        if (TextUtils.isEmpty(editTextTitle.text) || TextUtils.isEmpty(dateTask) || TextUtils.isEmpty(timeTask))
            return true
        return false
    } // Check Edit text date and time

    private fun isEdit(taskId: Long): Boolean {
        return taskId != -1L
    } // Check whether to be edited


}