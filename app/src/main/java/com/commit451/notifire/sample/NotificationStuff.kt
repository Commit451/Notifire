package com.commit451.notifire.sample

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build



object NotificationStuff {

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelName = "Test"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(context.getString(R.string.channel_id_default), channelName, importance)
            notificationChannel.description = "For testing things"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}