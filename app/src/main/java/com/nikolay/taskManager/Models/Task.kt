package com.nikolay.taskManager.Models

data class Task(
    var id: Long?,
    var title: String,
    var done: Boolean,
    var Priority: String,
    var PriorityGrade: Int,
    var date: String,
    var time: String
) {
    constructor() : this(-1, "", false, "", -1, "", "")


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false
        if (title != other.title) return false
        if (done != other.done) return false
        if (Priority != other.Priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + done.hashCode()
        result = 31 * result + Priority.hashCode()
        return result
    }

    override fun toString(): String {
        return "Task(id=$id, title='$title', done=$done, Priority='$Priority', PriorityGrade=$PriorityGrade, date='$date', time='$time')"
    }


}