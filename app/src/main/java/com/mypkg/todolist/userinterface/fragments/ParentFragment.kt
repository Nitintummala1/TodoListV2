package com.mypkg.todolist.userinterface.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mypkg.todolist.userinterface.MainActivityViewModel
import com.mypkg.todolist.userinterface.br.AlarmReceiver
import com.mypkg.todolist.data.model.MyCatInfo
import com.mypkg.todolist.data.model.MyTaskInfo
import com.mypkg.todolist.userinterface.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

open class ParentFragment : Fragment() {

    fun deleteTask(viewModel: MainActivityViewModel, myTaskInfo: MyTaskInfo, myCatInfo : MyCatInfo){
        CoroutineScope(Dispatchers.Main).launch {
            if(viewModel.getCountOfCategory(myCatInfo.categoryInformation)==1) {
                viewModel.deleteTaskAndCategory(myTaskInfo, myCatInfo)
            }else {
                viewModel.deleteTask(myTaskInfo)
            }
            val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("task_info", myTaskInfo)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), myTaskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
    }

    fun updateTaskStatus(viewModel: MainActivityViewModel, myTaskInfo: MyTaskInfo) {
        viewModel.updateTaskStatus(myTaskInfo)
        lifecycleScope.launch(Dispatchers.IO) {
            val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("task_info", myTaskInfo)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), myTaskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)

            if(myTaskInfo.status){
                alarmManager.cancel(pendingIntent)
            }else {
                val date = Date()
                if(myTaskInfo.date > date && myTaskInfo.date.seconds == 5){
                    val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
                    val basicPendingIntent = PendingIntent.getActivity(requireContext(), myTaskInfo.id, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
                    val clockInfo = AlarmManager.AlarmClockInfo(myTaskInfo.date.time, basicPendingIntent)
                    alarmManager.setAlarmClock(clockInfo, pendingIntent)
                }
            }
        }
    }

}