package ua.turskyi.covidnineteen.service.pushnotification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import ua.turskyi.covidnineteen.util.appIsInBackground
import ua.turskyi.covidnineteen.util.setNotificationInterval

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_POWER_MANAGER_TAG = "NotificationPowerManager:"
    }

    private val theSecond = 1000L

    override fun onReceive(context: Context, intent: Intent?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context.appIsInBackground()) {
                context.startForegroundService(Intent(context, NotificationService::class.java))
            } else {
                context.startService(Intent(context, this.javaClass))
            }
            context.setNotificationInterval()
        } else {
            val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as
                    PowerManager
            val wakeLock: PowerManager.WakeLock =
                powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, NOTIFICATION_POWER_MANAGER_TAG
                )
            /**
             * "wakeLock.acquire(5)" Acquires the wake lock with a timeout.
             *  Ensures that the device is on at the level requested when the wake lock was created. The
             *  lock will be released after the given timeout expires.
             *  https://developer.android.com/reference/android/os/PowerManager.WakeLock#acquire(long)
             */
            wakeLock.acquire(theSecond)
            context.startService(Intent(context, NotificationService::class.java))
            wakeLock.release()
        }
    }

    fun setNotificationInterval(context: Context) {
        val alarmManager: AlarmManager = context.getSystemService(ALARM_SERVICE) as
                AlarmManager
        val intentAlarm = Intent(context, NotificationReceiver::class.java)
        val data: Uri = Uri.parse(intentAlarm.toUri(Intent.URI_INTENT_SCHEME))
        intentAlarm.data = data
        val pendingIntentAlarm = PendingIntent.getBroadcast(
            context, 2,
            intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val minute = 1000L * 60
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + minute, pendingIntentAlarm
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + minute,
                minute,
                pendingIntentAlarm
            )
        }
    }
}