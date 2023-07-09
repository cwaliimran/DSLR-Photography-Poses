package com.cwnextgen.hdwallpapers.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cwnextgen.hdwallpapers.R
import com.cwnextgen.hdwallpapers.activities.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MyFirebaseInstanceService : FirebaseMessagingService() {
    var defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    var pendingIntent: PendingIntent? = null
    var intent: Intent? = null
    private val TAG = "MyFirebaseIn"

    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived: ${remoteMessage.data}")
        var title = ""
        var body = ""
        // Handle the data payload of the incoming message
        if (remoteMessage.data.isNotEmpty()) {
            // Process the data payload and take appropriate actions
            title = remoteMessage.data["title"].toString()
            body = remoteMessage.data["body"].toString()
        }

        if (remoteMessage.notification != null) {
            // Handle the notification payload of the incoming message
            remoteMessage.notification?.let { notification ->
                // Display the notification or handle it as needed
                title = remoteMessage.notification?.title.toString()
                body = remoteMessage.notification?.body.toString()
            }
        }

//        val msg = remoteMessage.data.get("title")
//


//        val filter = "filterName"
//        val intentSP =
//            Intent(filter) //If you need extra, add: intentSP.putExtra("extra","something");
//
//        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentSP)

        //String click_action = remoteMessage.getNotification().getClickAction();
        intent = Intent(this, SplashActivity::class.java)
        val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE


        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                100,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val channelId = getString(R.string.default_notification_channel_id)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val r = Random()
        val randomNo = r.nextInt(9999999)
        manager.notify(randomNo, builder.build())
    }
    // [END receive_message]
    // [START on_new_token]
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    override fun onNewToken(token: String) {

        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]
    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        ////Log.d(TAG, "scheduleJob: ");
        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        ////Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {
        //token is being sent on login and signup page
    }

    companion object {
        private const val TAG = "MyFirebaseInstanceService"
    }
}