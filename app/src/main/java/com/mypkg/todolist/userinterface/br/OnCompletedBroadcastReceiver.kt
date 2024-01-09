package com.mypkg.todolist.userinterface.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.mypkg.todolist.data.repository.TaskCategoryRepositoryImpl
import com.mypkg.todolist.data.model.MyTaskInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnCompletedBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var repository: TaskCategoryRepositoryImpl

    override fun onReceive(p0: Context?, p1: Intent?) {
        val myTaskInfo = p1?.getSerializableExtra("task_info") as? MyTaskInfo
        if (myTaskInfo != null) {
            myTaskInfo.status = true
        }
        CoroutineScope(IO).launch {
            myTaskInfo?.let {
                repository.updateTaskStatus(it)
            }
        }
        if (p0 != null && myTaskInfo != null) {
            NotificationManagerCompat.from(p0).cancel(null, myTaskInfo.id)
        }
    }
}