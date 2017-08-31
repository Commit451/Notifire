package com.commit451.notifire.sample

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * One time init things
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationStuff.createChannels(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.importance
        }
    }
}