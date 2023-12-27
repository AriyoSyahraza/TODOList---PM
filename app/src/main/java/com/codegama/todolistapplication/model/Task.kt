package com.codegama.todolistapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Task : Serializable {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var taskId = 0

    @JvmField
    @ColumnInfo(name = "taskTitle")
    var taskTitle: String? = null

    @JvmField
    @ColumnInfo(name = "date")
    var date: String? = null

    @JvmField
    @ColumnInfo(name = "taskDescription")
    var taskDescrption: String? = null

    @JvmField
    @ColumnInfo(name = "isComplete")
    var isComplete = false

    @JvmField
    @ColumnInfo(name = "firstAlarmTime")
    var firstAlarmTime: String? = null

    @JvmField
    @ColumnInfo(name = "secondAlarmTime")
    var secondAlarmTime: String? = null

    @JvmField
    @ColumnInfo(name = "lastAlarm")
    var lastAlarm: String? = null

    @JvmField
    @ColumnInfo(name = "event")
    var event: String? = null
}