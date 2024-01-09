package com.mypkg.todolist.data.db

import androidx.room.*
import com.mypkg.todolist.data.model.MyCatInfo
import com.mypkg.todolist.data.model.MyTaskInfo

@Database(entities = [MyTaskInfo::class, MyCatInfo::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskCategoryDao() : TaskCategoryDao
}