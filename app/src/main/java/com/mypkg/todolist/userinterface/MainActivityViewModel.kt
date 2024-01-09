package com.mypkg.todolist.userinterface

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mypkg.todolist.data.model.MyCatInfo
import com.mypkg.todolist.data.model.NoOfTaskForEachCategory
import com.mypkg.todolist.data.model.TaskCategoryInfo
import com.mypkg.todolist.data.model.MyTaskInfo
import com.mypkg.todolist.domain.TaskCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor (private val repository: TaskCategoryRepository) : ViewModel() {

    fun updateTaskStatus(task: MyTaskInfo) {
        viewModelScope.launch(IO) {
            repository.updateTaskStatus(task)
        }
    }

    fun deleteTask(task: MyTaskInfo) {
        viewModelScope.launch(IO) {
            repository.deleteTask(task)
        }
    }

    fun insertTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo) {
        viewModelScope.launch(IO) {
            repository.insertTaskAndCategory(myTaskInfo, myCatInfo)
        }
    }

    fun updateTaskAndAddCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo){
        viewModelScope.launch(IO) {
            repository.updateTaskAndAddCategory(myTaskInfo, myCatInfo)
        }
    }

    fun updateTaskAndAddDeleteCategory(
        myTaskInfo: MyTaskInfo,
        myCatInfoAdd: MyCatInfo,
        myCatInfoDelete: MyCatInfo
    ) {
        viewModelScope.launch(IO) {
            repository.updateTaskAndAddDeleteCategory(myTaskInfo, myCatInfoAdd, myCatInfoDelete)
        }
    }

    fun deleteTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo) {
        viewModelScope.launch(IO) {
            repository.deleteTaskAndCategory(myTaskInfo, myCatInfo)
        }
    }

    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> {
        return repository.getUncompletedTask()
    }

    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> {
        return repository.getCompletedTask()
    }

    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        return repository.getUncompletedTaskOfCategory(category)
    }

    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        return repository.getCompletedTaskOfCategory(category)
    }

    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>>{
        return repository.getNoOfTaskForEachCategory()
    }

    fun getCategories(): LiveData<List<MyCatInfo>>  {
        return repository.getCategories()
    }

    suspend fun getCountOfCategory(category: String): Int{
        var count: Int
        coroutineScope() {
            count = withContext(IO) { repository.getCountOfCategory(category) }
        }
        return count
    }

    fun getAlarms(currentTime : Date){
        CoroutineScope(Dispatchers.Main).launch {
            val list = repository.getActiveAlarms(currentTime)
            Log.d("DATA", list.toString())
        }
    }
}