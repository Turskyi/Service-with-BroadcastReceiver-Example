package ua.turskyi.covidnineteen.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.turskyi.covidnineteen.BuildConfig
import ua.turskyi.covidnineteen.R
import ua.turskyi.covidnineteen.service.common.NotificationStarter
import ua.turskyi.covidnineteen.util.doNotKillApp

/**
 * @Description
 * Every 10 minutes the app opens a browser with an arbitrary website, and every 20 minutes sends a
 * push notification with arbitrary text by clicking on which opens an arbitrary website.
 * After restarting the phone app should work as before.
 * When open the application should be displayed a blank screen.
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        startService(Intent(this, NotificationStarter::class.java))
        this.doNotKillApp()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                        NotificationManager

            val notificationChannel = NotificationChannel(
                BuildConfig.APPLICATION_ID,
                getString(R.string.channel_title),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = getString(R.string.channel_description)

            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
