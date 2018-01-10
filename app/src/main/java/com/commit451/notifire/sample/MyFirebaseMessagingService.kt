package com.commit451.notifire.sample

import android.util.Log
import com.commit451.notifire.notify
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage != null) {
            Log.d("notifire", "Sending push message from foreground")
            remoteMessage.notify(applicationContext, getString(R.string.app_name), R.mipmap.ic_launcher)
        }
    }
}