@file:JvmName("Notifire")

package com.commit451.notifire

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import java.util.*


private const val KEY_NOTIFICATION_CHANNEL = "gcm.notification.android_channel_id"
private const val KEY_MANIFEST_NOTIFICATION_CHANNEL = "com.google.firebase.messaging.default_notification_channel_id"

/**
 * Posts the [RemoteMessage] as a system notification.
 */
fun RemoteMessage.notify(context: Context, defaultTitle: String, @DrawableRes defaultNotificationResource: Int) {
    val notification = this.toNotification(context, defaultTitle, defaultNotificationResource)
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val id = this.notification?.tag?.hashCode() ?: UUID.randomUUID().toString().hashCode()
    manager.notify(id, notification)
}

/**
 * Transforms the [RemoteMessage] to a [Notification] for you to post yourself or modify as needed
 */
fun RemoteMessage.toNotification(context: Context, defaultTitle: String, @DrawableRes defaultNotificationResource: Int): Notification {
    return toNotificationBuilder(context, defaultTitle, defaultNotificationResource).build()
}

/**
 * Transforms the [RemoteMessage] to a [Notification.Builder] for you to post yourself or modify as needed
 */
fun RemoteMessage.toNotificationBuilder(context: Context, defaultTitle: String, @DrawableRes defaultNotificationResource: Int): NotificationCompat.Builder {
    var soundUri: Uri? = null
    if (notification?.sound != null) {
        soundUri = if (notification?.sound == "default") {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else {
            val sound = Uri.parse("android.resource://${context.packageName}/raw/${notification?.sound}")
            sound
        }
    }
    val icon = notification?.icon

    var resourceId: Int? = null
    if (icon != null) {
        resourceId = context.resources.getIdentifier(icon, "drawable", context.packageName)
        if (resourceId == 0) {
            resourceId = null
        }
    }
    var color: Int? = null
    if (notification?.color != null) {
        color = Color.parseColor(notification?.color)
    }
    val title = notification?.title ?: defaultTitle
    val bundle = getBundle(this)
    val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
    //found default channel id via experimentation
    var channelId = "fcm_fallback_notification_channel"
    if (bundle != null && bundle.containsKey(KEY_NOTIFICATION_CHANNEL)) {
        channelId = bundle.getString(KEY_NOTIFICATION_CHANNEL)
    } else if (applicationInfo.metaData.containsKey(KEY_MANIFEST_NOTIFICATION_CHANNEL)) {
        channelId = applicationInfo.metaData.getString(KEY_MANIFEST_NOTIFICATION_CHANNEL)
    }

    val builder = NotificationCompat.Builder(context, channelId)

    builder
            .setContentTitle(title)
            .setContentText(notification?.body)
            .setAutoCancel(true)
    if (resourceId != null) {
        builder.setSmallIcon(resourceId)
    } else {
        builder.setSmallIcon(defaultNotificationResource)
    }
    if (soundUri != null) {
        builder.setSound(soundUri)
    }
    if (color != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder.setColor(color)
    }

    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    for (key in this.data.keys) {
        intent?.putExtra(key, this.data[key])
    }
    val resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    builder.setContentIntent(resultPendingIntent)
    return builder
}

private fun getBundle(remoteMessage: RemoteMessage): Bundle? = remoteMessage.toIntent().extras


