package com.xm.commoncomponent

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity


/**
 * https://www.cnblogs.com/punkisnotdead/p/4881771.html
 * https://www.cnblogs.com/travellife/p/Android-Notification-xiang-jie.html
 */
class NotificationAct : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        //定义一个PendingIntent点击Notification后启动一个Activity
        val it = Intent(this, MainActivity::class.java)
        val pit = PendingIntent.getActivity(this, 0, it, 0)
        val LargeBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        //设置图片,通知标题,发送时间,提示方式等属性
        val mBuilder = NotificationCompat.Builder(this)
        mBuilder.setContentTitle("叶良辰")                         //标题
                .setContentText("我有一百种方法让你呆不下去~")     //内容
                .setSubText("——记住我叫叶良辰")                  //内容下面的一小段文字
                .setTicker("收到叶良辰发送过来的信息~")            //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())               //设置通知时间
                .setSmallIcon(R.mipmap.ic_launcher)                //设置小图标
                .setLargeIcon(LargeBitmap)                         //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)           //设置默认的三色灯与振动器
//                .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.biaobiao))  //设置自定义的提示音
                .setAutoCancel(true)                               //设置点击后取消Notification
                .setContentIntent(pit)                             //设置PendingIntent
        val notify1 = mBuilder.build()
        val NOTIFYID_1 = 1
        val mNManager: NotificationManager? = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        mNManager?.notify(NOTIFYID_1, notify1)
    }
}
