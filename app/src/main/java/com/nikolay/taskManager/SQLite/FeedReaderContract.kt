package com.nikolay.taskManager.SQLite

import android.provider.BaseColumns

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val DbName = "task.db"
        const val DbVersion = 1
        const val TABLE_TASK = "task"
        const val COLUMN_TASK_TITLE = "title"
        const val COLUMN_TASK_DONE = "done"
        const val COLUMN_TASK_PRIORITY = "priority"
        const val COLUMN_TASK_PRIORITY_GRADE = "priority_grade"
        const val COLUMN_TASK_TIME = "time"
    }


    const val SQL_CREATE_ENTRIES_TASK =
        "CREATE TABLE ${FeedEntry.TABLE_TASK} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_TASK_TITLE} TEXT NOT NULL," +
                "${FeedEntry.COLUMN_TASK_DONE} BOOLEAN," +
                "${FeedEntry.COLUMN_TASK_PRIORITY} TEXT, " +
                "${FeedEntry.COLUMN_TASK_PRIORITY_GRADE} INTEGER , " +
                "${FeedEntry.COLUMN_TASK_TIME} DATETIME );"
}