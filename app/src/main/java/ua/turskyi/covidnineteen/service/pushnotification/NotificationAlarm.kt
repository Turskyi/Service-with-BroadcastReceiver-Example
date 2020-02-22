package ua.turskyi.covidnineteen.service.pushnotification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import ua.turskyi.covidnineteen.util.appIsInBackground

class NotificationAlarm : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_POWER_MANAGER_TAG = "NotificationPowerManager:"
    }

    private val theSecond = 1000L

    override fun onReceive(context: Context?, intent: Intent?) {
        val powerManager = context?.getSystemService(Context.POWER_SERVICE) as
                PowerManager
        val wakeLock =
            powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, NOTIFICATION_POWER_MANAGER_TAG)

        /**
         * "wakeLock.acquire(5)" Acquires the wake lock with a timeout.
         *  Ensures that the device is on at the level requested when the wake lock was created. The
         *  lock will be released after the given timeout expires.
         *  https://developer.android.com/reference/android/os/PowerManager.WakeLock#acquire(long)
         */
        wakeLock.acquire(theSecond)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appIsInBackground(context)) {
                context.startForegroundService(Intent(context, NotificationService::class.java))
            } else {
                context.startService(Intent(context, NotificationService::class.java))
            }
        } else {
            context.startService(Intent(context, NotificationService::class.java))
        }
        wakeLock.release()
    }

    fun setAnAlarmInterval(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as
                AlarmManager
        val intentAlarm = Intent(context, NotificationAlarm::class.java)
        val data: Uri = Uri.parse(intentAlarm.toUri(Intent.URI_INTENT_SCHEME))
        intentAlarm.data = data
        val pendingIntentAlarm = PendingIntent.getBroadcast(
            context, 2,
            intentAlarm, 0
        )

        val minute = theSecond * 60
        val notificationInterval = minute * 20
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + notificationInterval,
            notificationInterval,
            pendingIntentAlarm
        )
    }
}