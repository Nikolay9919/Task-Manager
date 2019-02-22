package com.example.myapplication.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.myapplication.Models.Task

class FeedReaderDbHelper(context: Context) :
    SQLiteOpenHelper(context, FeedReaderContract.FeedEntry.DbName, null, FeedReaderContract.FeedEntry.DbVersion) {
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES_TASK)
        db?.execSQL(FeedReaderContract.SQL_INSERT_TASK)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_TITLE, task.title)
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_DONE, task.done)
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY, task.Priority)
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY_GRADE, task.PriorityGrade)
        }
        val newRow = db.insert(FeedReaderContract.FeedEntry.TABLE_TASK, null, values)
    }

    @SuppressLint("Recycle")
    fun getAllTasks(): ArrayList<Task>? {
        val db = readableDatabase

        val selectQuery: String = "SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_TASK
        val cursor = db.rawQuery(selectQuery, null)


        val taskList = ArrayList<Task>()



        Log.d("Cursor", cursor.toString())


        if (cursor.moveToFirst())
            do {
                val task = Task(
                    cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_TITLE)),
                    changeToBool(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_DONE))),
                    cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY)),
                    cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY_GRADE))
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        Log.d("AllTasksCursor", taskList.toString())
        return taskList
    }

    @SuppressLint("Recycle")
    fun getTask(taskId: Long): Task {
        val db = readableDatabase
        val selectQuery: String = "SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_TASK + " WHERE " +
                BaseColumns._ID + " = " + taskId
        val cursor = db.rawQuery(selectQuery, null)
        var task = Task()
        if (cursor != null && cursor.moveToFirst()) {

            Log.d("cursor", cursor.toString())
            task = Task(
                cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)),
                cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_TITLE)),
                equals(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_DONE))),
                cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY)),
                cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY_GRADE))
            )
        }

        return task
    }

    fun updateTask(task: Task) {
        Log.d("updateTask", task.toString())
        val contentValues = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_TITLE, task.title)
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_DONE, task.done)
            put(FeedReaderContract.FeedEntry.COLUMN_TASK_PRIORITY, task.Priority)

        }
        val db = writableDatabase
        Log.d("cv", contentValues.toString())
        db.update(
            FeedReaderContract.FeedEntry.TABLE_TASK, contentValues,
            BaseColumns._ID + " = " + task.id, null
        )
        Log.d("task", getTask(task.id!!).toString())
    }

    fun deleteTask(taskId: Long) {
        val selection = "${BaseColumns._ID} LIKE " + taskId
        val db = writableDatabase
        db.delete(FeedReaderContract.FeedEntry.TABLE_TASK, selection, null)
    }

    private fun changeToBool(int: Int): Boolean {
        return int == 1
    }
}


