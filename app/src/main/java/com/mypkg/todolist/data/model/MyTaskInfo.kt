package com.mypkg.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "taskInfo")
data class MyTaskInfo(
    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var status : Boolean,
    var category: String,
    var ttitle: String,
    var description : String,
    var date : Date,
    var priority : Int
) : Serializable

