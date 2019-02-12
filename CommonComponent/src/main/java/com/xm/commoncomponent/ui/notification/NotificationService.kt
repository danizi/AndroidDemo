package com.xm.commoncomponent.ui.notification

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.xm.commoncomponent.R

class NotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        iniNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun iniNotification() {
        val remoteView = RemoteViews(packageName, R.layout.notification_media)//自定义通知布局
        remoteView.setTextViewText(R.id.musicName_textView, "三国杀-汪苏泷")
        remoteView.setTextViewText(R.id.playTime_textView, "1:30")
        remoteView.setTextViewText(R.id.musicTime_textView, "3:25")
        val builder = NotificationCompat.Builder(applicationContext)
                .setTicker("")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteView)
                .setContentTitle("title")
                .setContentText("text")

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1111, builder.build())
    }
}