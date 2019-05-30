# Notifire

[![Build Status](https://travis-ci.org/Commit451/Notifire.svg?branch=master)](https://travis-ci.org/Commit451/Notifire) [![](https://jitpack.io/v/Commit451/Notifire.svg)](https://jitpack.io/#Commit451/Notifire)

When you use Firebase for push notifications, sometimes you might run into the unexpected result that if the app is in the foreground, the [notification will not be posted to the device](https://firebase.google.com/docs/cloud-messaging/concept-options). Instead, you must do the work yourself. But for some applications, it still makes sense to just post the notification as it would if it were in the background. That is what Notifire was built for.

## Usage
```kotlin
class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        //posts the notification right away with the configuration from Firebase
        remoteMessage?.notify(applicationContext, getString(R.string.app_name), R.mipmap.ic_launcher)
    }
}
```
You also need to set up a default notification channel for Firebase to use when pushing in your manifest, like so:
```xml
<meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="@string/default_notification_channel_id"/>
```
Make sure you actually create this channel in your application `onCreate`

## Supported
See [the docs](https://firebase.google.com/docs/cloud-messaging/http-server-ref) for how to configure a notification payload
- title
- body
- icon
- sound
- tag
- color
- android_channel_id

## Limitations
- If you set an invalid sound on your notification payload, in the background, Firebase will play the default sound if the raw resource is not found, but when the message is posted in the foreground with Notifire, it will play no sound if the raw resource is not found. This is an uncommon case since you should only send valid resource names in the payload, but something worth noting.
- This library uses a package private variable `zzdp` within `RemoteMessage`, and has been tested with Firebase Messaging 17.0.0. If you are on a different version than 17.0.0, this library may not work as expected. Use caution when upgrading or downgrading.

## Contributing
To build this project with the sample, you will need to create your own Firebase project with the same package name as the sample: `com.commit451.notifire.sample` and paste the `google-services.json` into the `app` folder. The sample should then build properly.

License
--------

    Copyright 2019 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
