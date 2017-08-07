package com.commit451.notifire

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import java.util.*

/**
 * Posts the [RemoteMessage] as a system notification.
 */
fun RemoteMessage.notify(context: Context, defaultTitle: String, @DrawableRes defaultNotificationResource: Int) {
    val notification = this.toNotification(context, defaultTitle, defaultNotificationResource)
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val id = this.notification.tag?.hashCode() ?: UUID.randomUUID().toString().hashCode()
    manager.notify(id, notification)
}

/**
 * Transforms the [RemoteMessage] to a [Notification] for you to post yourself or modify as needed
 */
fun RemoteMessage.toNotification(context: Context, defaultTitle: String, @DrawableRes defaultNotificationResource: Int): Notification {
    var soundUri: Uri? = null
    if (notification.sound != null) {
        if (notification.sound == "default") {
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else {
            val sound = Uri.parse("android.resource://${context.packageName}/raw/${notification.sound}")
            soundUri = sound
        }
    }
    val icon = notification.icon

    var resourceId: Int? = null
    if (icon != null) {
        resourceId = context.resources.getIdentifier(icon, "drawable", context.packageName)
        if (resourceId == 0) {
            resourceId = null
        }
    }
    var color: Int? = null
    if (notification.color != null) {
        color = Color.parseColor(notification.color)
    }
    val title = notification.title ?: defaultTitle
    val builder = NotificationCompat.Builder(context)
            .setContentTitle(title)
            .setContentText(notification.body)
            .setAutoCancel(true)
    if (resourceId != null) {
        builder.setSmallIcon(resourceId)
    } else {
        builder.setSmallIcon(defaultNotificationResource)
    }
    if (soundUri != null) {
        builder.setSound(soundUri)
    }
    if (color != null) {
        builder.setColor(color)
    }

    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    for (key in this.data.keys) {
        intent.putExtra(key, this.data[key])
    }
    val resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    builder.setContentIntent(resultPendingIntent)
    return builder.build()
}
