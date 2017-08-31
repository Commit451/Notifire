package com.google.firebase.messaging

import android.os.Bundle

/**
 * Plz ignore
 */
object Util {

    fun getBundle(remoteMessage: RemoteMessage): Bundle? {
        return remoteMessage.mBundle
    }
}