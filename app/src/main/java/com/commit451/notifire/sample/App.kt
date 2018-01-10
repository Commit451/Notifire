package com.commit451.notifire.sample

import android.app.Application

/**
 * One time init things
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationStuff.createChannels(this)
    }
}