package com.xm.commoncomponent.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        val ACTION_NOTIFICATION_PRE = "action.notification.pre"
        val ACTION_NOTIFICATION_NEXT = "action.notification.next"
        val ACTION_NOTIFICATION_START = "action.notification.start"
        val ACTION_NOTIFICATION_STOP = "action.notification.stop"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_NOTIFICATION_PRE -> {

            }
            ACTION_NOTIFICATION_NEXT -> {

            }
            ACTION_NOTIFICATION_START -> {

            }
            ACTION_NOTIFICATION_STOP -> {

            }
        }
    }
}