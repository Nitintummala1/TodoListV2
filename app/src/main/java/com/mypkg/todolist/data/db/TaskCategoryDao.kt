package com.mypkg.todolist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mypkg.todolist.data.model.MyCatInfo
import com.mypkg.todolist.data.model.NoOfTaskForEachCategory
import com.mypkg.todolist.data.model.TaskCategoryInfo
import com.mypkg.todolist.data.model.MyTaskInfo
import java.util.*

@Dao
interface TaskCategoryDao {
    @Insert
    suspend fun insertTask(task : MyTaskInfo) : Long

    @Update
    suspend fun updateTaskStatus(task: MyTaskInfo) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(myCatInfo: MyCatInfo) : Long

    @Delete
    suspend fun deleteTask(task: MyTaskInfo)

    @Delete
    suspend fun deleteCategory(myCatInfo: MyCatInfo)

    @Transaction
    suspend fun insertTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo){
        insertTask(myTaskInfo)
        insertCategory(myCatInfo)
    }

    @Transaction
    suspend fun updateTaskAndAddCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo){
        updateTaskStatus(myTaskInfo)
        insertCategory(myCatInfo)
    }

    @Transaction
    suspend fun updateTaskAndAddDeleteCategory(myTaskInfo: MyTaskInfo, myCatInfoAdd: MyCatInfo, myCatInfoDelete: MyCatInfo){
        updateTaskStatus(myTaskInfo)
        insertCategory(myCatInfoAdd)
        deleteCategory(myCatInfoDelete)
    }

    @Transaction
    suspend fun deleteTaskAndCategory(myTaskInfo: MyTaskInfo, myCatInfo: MyCatInfo){
        deleteTask(myTaskInfo)
        deleteCategory(myCatInfo)
    }

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "ORDER BY date")
    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 1 " +
            "ORDER BY date")
    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "AND category =:category " +
            "ORDER BY date")
    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 1 " +
            "AND category = :category " +
            "ORDER BY date")
    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>

    @Query("SELECT " +
            "taskInfo.category as category," +
            "Count(*) as count, " +
            "categoryInfo.color as color  " +
            "FROM taskInfo, categoryInfo " +
            "WHERE taskInfo.category == categoryInfo.categoryInformation " +
            "AND " +
            "taskInfo.status = 0 " +
            "GROUP BY category " +
            "ORDER BY count DESC, category")

    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>>

    @Query("SELECT * " +
            "FROM categoryInfo")
    fun getCategories(): LiveData<List<MyCatInfo>>

    @Query("SELECT * " +
            "FROM taskInfo")
    fun getTasks(): LiveData<List<MyTaskInfo>>

    @Query("SELECT COUNT(*) " +
            "FROM taskInfo " +
            "WHERE category = :category ")
    fun getCountOfCategory(category: String) : Int

    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "AND date > :currentTime")
    fun getActiveAlarms(currentTime : Date) : List<MyTaskInfo>

}