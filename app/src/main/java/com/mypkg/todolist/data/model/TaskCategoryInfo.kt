package com.mypkg.todolist.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class TaskCategoryInfo(

    @Embedded val myTaskInfo: MyTaskInfo,
    @Relation(
        parentColumn = "category",
        entityColumn = "categoryInformation"
    )
    val myCatInfo: List<MyCatInfo>
) : Serializable
