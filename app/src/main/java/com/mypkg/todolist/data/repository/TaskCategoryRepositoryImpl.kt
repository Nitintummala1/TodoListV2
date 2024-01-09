package com.mypkg.todolist.data.repository

import androidx.lifecycle.LiveData
import com.mypkg.todolist.data.db.TaskCategoryDao
import com.mypkg.todolist.domain.TaskCategoryRepository
import com.mypkg.todolist.data.model.MyCatInfo
import com.mypkg.todolist.data.model.NoOfTaskForEachCategory
import com.mypkg.todolist.data.model.TaskCategoryInfo
import com.mypkg.todolist.data.model.MyTaskInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class TaskCategoryRepositoryImpl @Inject constructor(private val taskCategoryDao: TaskCategoryDao) :
    TaskCategoryRepository {

    override suspend fun updateTaskStatus(task: MyTaskInfo) : Int{
        return taskCategoryDao.updateTaskStatus(task)
    }

    override suspend fun deleteTask(task: MyTaskInfo) {
        taskCategoryDao.deleteTask(task)
    }

    override suspend fun insertTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo) {
        taskCategoryDao.insertTaskAndCategory(myTaskInfo, myCatInfo)
    }

    override suspend fun deleteTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo) {
        taskCategoryDao.deleteTaskAndCategory(myTaskInfo, myCatInfo)
    }

    override suspend fun updateTaskAndAddDeleteCategory(
        myTaskInfo: MyTaskInfo,
        myCatInfoAdd: MyCatInfo,
        myCatInfoDelete: MyCatInfo
    ) {
        taskCategoryDao.updateTaskAndAddDeleteCategory(myTaskInfo, myCatInfoAdd, myCatInfoDelete)
    }

    override suspend fun updateTaskAndAddCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo) {
        taskCategoryDao.updateTaskAndAddCategory(myTaskInfo, myCatInfo)
    }

    override fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getUncompletedTask()
    override fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getCompletedTask()
    override fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getUncompletedTaskOfCategory(category)
    override fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getCompletedTaskOfCategory(category)
    override fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>> = taskCategoryDao.getNoOfTaskForEachCategory()
    override fun getCategories(): LiveData<List<MyCatInfo>> = taskCategoryDao.getCategories()
    override suspend fun getCountOfCategory(category: String): Int = taskCategoryDao.getCountOfCategory(category)
    override suspend fun getActiveAlarms(currentTime: Date): List<MyTaskInfo> {
        var list: List<MyTaskInfo>
        coroutineScope {
            list = withContext(IO){taskCategoryDao.getActiveAlarms(currentTime)}
        }
        return list
    }
}