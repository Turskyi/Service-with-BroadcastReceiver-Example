package ua.turskyi.covidnineteen.service.pushnotification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ua.turskyi.covidnineteen.BuildConfig.APPLICATION_ID
import ua.turskyi.covidnineteen.R

class NotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        openWebsite()
        fireNotificator()
        return START_NOT_STICKY
    }

    private fun fireNotificator() {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationBuilder = NotificationCompat.Builder(
                applicationContext,
                APPLICATION_ID
            )

            val bigTextStyle: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.setBigContentTitle(getString(R.string.notification_title))
            notificationBuilder.setContentIntent(openWebsite())
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
            notificationBuilder.setContentText(getString(R.string.notification_content_text))
            notificationBuilder.setStyle(bigTextStyle)
            notificationBuilder.setSound(defaultSoundUri)

            notificationBuilder.setChannelId(APPLICATION_ID)


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(2, notificationBuilder.build())
        } else {
            val notifyManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notification: Notification = NotificationCompat.Builder(
                this,
                APPLICATION_ID
            ).setContentTitle(getString(R.string.notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentText(getString(R.string.notification_content_text))
                .setContentIntent(openWebsite())
                .setAutoCancel(true)
                .build()
            notifyManager.notify(2, notification)
        }
    }

    private fun openWebsite(): PendingIntent {
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_link)))
        return PendingIntent.getActivity(
            this, 3,
            intentBrowser, 0
        )
    }
}