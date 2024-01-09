package com.mypkg.todolist.userinterface.br

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mypkg.todolist.userinterface.MainActivity
import com.mypkg.todolist.R
import com.mypkg.todolist.data.model.MyTaskInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onReceive(p0: Context?, p1: Intent?) {
        val myTaskInfo = p1?.getSerializableExtra("task_info") as? MyTaskInfo
        if(sharedPreferences.getBoolean(myTaskInfo?.priority.toString(), true)){
            val tapResultIntent = Intent(p0, MainActivity::class.java)
            tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent: PendingIntent = getActivity( p0,0,tapResultIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

            val intent1 = Intent(p0, OnCompletedBroadcastReceiver::class.java).apply {
                putExtra("task_info", myTaskInfo)
            }
            val pendingIntent1: PendingIntent? =
                myTaskInfo?.let { getBroadcast(p0, it.id,intent1,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE) }
            val action1 : NotificationCompat.Action = NotificationCompat.Action.Builder(0,"Completed",pendingIntent1).build()

            val notification = p0?.let {
                NotificationCompat.Builder(it, "to_do_list")
                    .setContentTitle("Task Reminder")
                    .setContentText(myTaskInfo?.description)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .addAction(action1)
                    .build()
            }
            notificationManager = p0?.let { NotificationManagerCompat.from(it) }
            notification?.let { myTaskInfo?.let { it1 -> notificationManager?.notify(it1.id, it) } }
        }
    }
}