/**
 *   Copyright 2020 Geek Zone UK
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package zone.geek.membership.pushnotification

import android.app.NotificationManager
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import zone.geek.membership.R
import java.util.*

class MessagingService : FirebaseMessagingService() {

    companion object {
        private const val DEFAULT_CHANNEL_ID = "GZDefaultChannelID"
        private val TAG = this::class.java.canonicalName
        private const val RANDOM_BOUND = 6000
        private const val BODY = "body"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.let { message ->

            Log.i(TAG, message.data[BODY] as String)

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationId = Random().nextInt(RANDOM_BOUND)

            val clickAction = remoteMessage.data["click_action"]
            val intent = Intent(clickAction)
            val pendingIntent =
                getActivity(
                    applicationContext,
                    0,
                    intent,
                    FLAG_ONE_SHOT
                )

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_gz_icon)  //a resource for your custom small icon
                .setNumber(1)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorYellowLight))
                .setContentTitle(message.data[TITLE]) //the "title" value we sent in data and notification
                .setStyle(NotificationCompat.BigTextStyle().bigText(message.data[BODY]))
                .setContentText(message.data[MESSAGE])
                .setAutoCancel(false)  //dismisses the notification on click?
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(
                notificationId /* ID of notification */,
                notificationBuilder.build()
            )
        }
    }
}
