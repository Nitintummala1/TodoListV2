package com.mypkg.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "categoryInfo")
data class MyCatInfo(
    @PrimaryKey
    var categoryInformation: String,
    var color: String
) : Serializable
