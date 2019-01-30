package com.xm.commoncomponent

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.RemoteViews


/**
 * https://www.cnblogs.com/punkisnotdead/p/4881771.html
 * https://www.cnblogs.com/travellife/p/Android-Notification-xiang-jie.html
 */
class NotificationAct : AppCompatActivity(), ISetup, View.OnClickListener {

    private var btnNotification1: Button? = null
    private var btnNotification2: Button? = null
    private var btnNotification3: Button? = null
    private var btnDelNotification: Button? = null
    private var btnDelAllNotification: Button? = null
    private var btnProcessNotification: Button? = null
    private var btnFloatNotification: Button? = null
    private var btnMediaNotification: Button? = null
    private var btnCustomNotification: Button? = null


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        findViews()
        initDisplay()
        initEvent()
        initData()
    }

    override fun findViews() {
        btnNotification1 = findViewById(R.id.btn_notification1)
        btnNotification2 = findViewById(R.id.btn_notification2)
        btnNotification3 = findViewById(R.id.btn_notification3)
        btnDelNotification = findViewById(R.id.btn_del_notification)
        btnDelAllNotification = findViewById(R.id.btn_del_all_notification)
        btnProcessNotification = findViewById(R.id.btn_process_notification)
        btnFloatNotification = findViewById(R.id.btn_float_notification)
        btnMediaNotification = findViewById(R.id.btn_media_notification)
        btnCustomNotification = findViewById(R.id.btn_custom_notification)
    }

    override fun initEvent() {
        btnNotification1?.setOnClickListener(this)
        btnNotification2?.setOnClickListener(this)
        btnNotification3?.setOnClickListener(this)
        btnDelNotification?.setOnClickListener(this)
        btnDelAllNotification?.setOnClickListener(this)
        btnProcessNotification?.setOnClickListener(this)
        btnFloatNotification?.setOnClickListener(this)
        btnMediaNotification?.setOnClickListener(this)
        btnCustomNotification?.setOnClickListener(this)
    }

    companion object {
        val ID_NOTIFICATION_1 = 1
        val ID_NOTIFICATION_2 = 2
        val ID_NOTIFICATION_3 = 3
        val ID_PROCESS_NOTIFICATION = 4
        val ID_FLOAT_NOTIFICATION = 5
        val ID_MEDIA_NOTIFICATION = 6
        val ID_CUSTOM_NOTIFICATION = 7
    }

    override fun onClick(v: View?) {
        when (v) {
            btnNotification1 -> {
                //简单创建方式一
                sendNotify()
            }
            btnNotification2 -> {
                //简单创建方式二
                sendNotify2()
            }
            btnNotification3 -> {
                //扩展创建
                sendNotify3()
            }
            btnDelNotification -> {
                //删除第一个通知 根据id来寻找对应通知
                delNotification()
            }
            btnDelAllNotification -> {
                //删除所有通知
                delAllNotification()
            }
            btnProcessNotification -> {
                //发送进度通知
                sendProcessNotification()
            }
            btnFloatNotification -> {
                //发送浮点通知
                sendFloatNotification()
            }
            btnMediaNotification -> {
                //发送媒体通知
                sendMediaNotification()
            }
            btnCustomNotification -> {
                //自定义通知
                sendCustomNotification()
            }
        }
    }

    private fun sendCustomNotification() {
        /**
         * 自定义通知布局对布局是有限定的
         * https://blog.csdn.net/robert__zhang/article/details/51332958
         */
        //1 构建用户点击跳转的意图
        val taskStackBuilder = TaskStackBuilder.create(this)
        val nextIntent = Intent(this, MainActivity::class.java)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(nextIntent)
        val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        //2 建造notification
        val builder = NotificationCompat.Builder(this)
        val remoteView = RemoteViews(packageName, R.layout.notification_custom)//自定义通知布局
        builder.setSmallIcon(R.mipmap.ic_launcher)       //通知标题 必须设置
                .setPriority(PRIORITY_DEFAULT)           //通知优先级 有五个优先级别，范围从 PRIORITY_MIN (-2) 到 PRIORITY_MAX (2)；如果未设置，则优先级默认为 PRIORITY_DEFAULT (0)。
                .setContentIntent(pendingIntent)         //用户点击跳转的意图设置
                .setContent(remoteView)

        //3 更新通知notification
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ID_CUSTOM_NOTIFICATION, builder.build())
    }

    private fun sendMediaNotification() {

    }

    private fun sendFloatNotification() {

    }

    private fun sendProcessNotification() {
        //1 构建用户点击跳转的意图
        val taskStackBuilder = TaskStackBuilder.create(this)
        val nextIntent = Intent(this, MainActivity::class.java)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(nextIntent)
        val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        //2 建造notification
        val builder = NotificationCompat.Builder(this)
        builder.setContentTitle("进度notification标题")  //通知标题 必须设置
                .setContentText("进度notification内容")  //通知内容 必须设置
                .setSmallIcon(R.mipmap.ic_launcher)      //通知标题 必须设置
                .setPriority(PRIORITY_DEFAULT)           //通知优先级 有五个优先级别，范围从 PRIORITY_MIN (-2) 到 PRIORITY_MAX (2)；如果未设置，则优先级默认为 PRIORITY_DEFAULT (0)。
                .setContentIntent(pendingIntent)         //用户点击跳转的意图设置
                .setProgress(100, 0, false)

        //3 更新通知notification
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(4, builder.build())

        //4 更新耗时操作进度
        val max = 100
        var process = 0
        Thread(Runnable {
            while (process < max) {
                builder.setProgress(max, process, false)
                process += 10
                notificationManager.notify(ID_PROCESS_NOTIFICATION, builder.build())
                Thread.sleep(1000)
            }
            builder.setProgress(0, 0, false)
            builder.setContentText("耗时操作已完成")
            notificationManager.notify(ID_PROCESS_NOTIFICATION, builder.build())
        }).start()
    }

    private fun delAllNotification() {
        //删除所有通知
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun delNotification() {
        //删除第一个通知
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION_1)
    }

    override fun initDisplay() {

    }

    override fun initData() {

    }

    private fun sendNotify() {
        //1 构建用户点击跳转的意图
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)

        //2 建造notification
        val builder = NotificationCompat.Builder(this)
        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        builder.setContentTitle("通知标题1")                       //标题
                .setContentText("通知内容1")                       //内容
                .setSubText("通知子标题")                          //内容下面的一小段文字
                .setTicker("收到通知标题1发送过来的信息~")         //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())               //设置通知时间
                .setSmallIcon(R.mipmap.ic_launcher)                //设置小图标
                .setLargeIcon(icon)                                //设置大图标
                .setAutoCancel(true)                               //设置点击后取消Notification
                .setContentIntent(pendingIntent)                   //用户点击跳转的意图设置

        //3 更新通知notification
        val notification = builder.build()
        val notificationManager: NotificationManager? = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.notify(ID_NOTIFICATION_1, notification)
    }

    private fun sendNotify2() {
        //1 构建用户点击跳转的意图
        val taskStackBuilder = TaskStackBuilder.create(this)
        val nextIntent = Intent(this, MainActivity::class.java)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(nextIntent)
        val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        //2 建造notification
        val builder = NotificationCompat.Builder(this)
        builder.setContentTitle("通知标题2")              //通知标题 必须设置
                .setContentText("通知内容2")              //通知内容 必须设置
                .setSmallIcon(R.mipmap.ic_launcher)      //通知标题 必须设置
                .setPriority(PRIORITY_DEFAULT)           //通知优先级 有五个优先级别，范围从 PRIORITY_MIN (-2) 到 PRIORITY_MAX (2)；如果未设置，则优先级默认为 PRIORITY_DEFAULT (0)。
                .setContentIntent(pendingIntent)         //用户点击跳转的意图设置
        val notification = builder.build() //构建notification

        //3 更新通知notification
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ID_NOTIFICATION_2, notification)
    }

    private fun sendNotify3() {
        //1 构建用户点击跳转的意图
        val taskStackBuilder = TaskStackBuilder.create(this)
        val nextIntent = Intent(this, MainActivity::class.java)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(nextIntent)
        val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        //2 建造notification
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine("addLine1")
        inboxStyle.addLine("addLine2")
        inboxStyle.setBigContentTitle("大大的内容")
        inboxStyle.setSummaryText("描述内容")
        val builder = NotificationCompat.Builder(this)
        builder.setContentTitle("扩展notification标题")              //通知标题 必须设置
                .setContentText("扩展notification内容")              //通知内容 必须设置
                .setSmallIcon(R.mipmap.ic_launcher)      //通知标题 必须设置
                .setPriority(PRIORITY_DEFAULT)           //通知优先级 有五个优先级别，范围从 PRIORITY_MIN (-2) 到 PRIORITY_MAX (2)；如果未设置，则优先级默认为 PRIORITY_DEFAULT (0)。
                .setContentIntent(pendingIntent)         //用户点击跳转的意图设置
                .setStyle(inboxStyle)
        val notification = builder.build() //构建notification

        //3 更新通知notification
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ID_NOTIFICATION_3, notification)
    }
}
