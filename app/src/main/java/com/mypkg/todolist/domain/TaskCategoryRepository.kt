package com.mypkg.todolist.domain

import androidx.lifecycle.LiveData
import com.mypkg.todolist.data.model.MyCatInfo
import com.mypkg.todolist.data.model.NoOfTaskForEachCategory
import com.mypkg.todolist.data.model.TaskCategoryInfo
import com.mypkg.todolist.data.model.MyTaskInfo
import java.util.*

interface TaskCategoryRepository {
    suspend fun updateTaskStatus(task: MyTaskInfo) : Int
    suspend fun deleteTask(task: MyTaskInfo)
    suspend fun insertTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo)
    suspend fun deleteTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo)
    suspend fun updateTaskAndAddDeleteCategory(myTaskInfo: MyTaskInfo, myCatInfoAdd: MyCatInfo, myCatInfoDelete: MyCatInfo)
    suspend fun updateTaskAndAddCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo)
    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>>
    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>>
    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>
    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>
    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>>
    fun getCategories(): LiveData<List<MyCatInfo>>
    suspend fun getCountOfCategory(category: String) : Int
    suspend fun getActiveAlarms(currentTime : Date) : List<MyTaskInfo>
}