package ua.turskyi.covidnineteen.service.websiteopener

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

class WebsiteAlarm : BroadcastReceiver() {

    companion object {
        const val WEBSITE_POWER_MANAGER_TAG = "WebsitePowerManager:"
    }

    private val theSecond = 1000L

    override fun onReceive(context: Context, intent: Intent?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context.appIsInBackground()) {
                context.startForegroundService(Intent(context, WebsiteService::class.java))
            } else {
                context.startService(Intent(context, WebsiteService::class.java))
            }
            setAnAlarmInterval(context)
        } else {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as
                    PowerManager
            val wakeLock =
                powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, WEBSITE_POWER_MANAGER_TAG
                )

            /**
             * "wakeLock.acquire()" Acquires the wake lock with a timeout.
             *  Ensures that the device is on at the level requested when the wake lock was created. The
             *  lock will be released after the given timeout expires.
             *  https://developer.android.com/reference/android/os/PowerManager.WakeLock#acquire(long)
             */
            wakeLock.acquire(theSecond)
            context.startService(Intent(context, WebsiteService::class.java))
            wakeLock.release()
        }
    }

    fun setAnAlarmInterval(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as
                AlarmManager
        val intentAlarm = Intent(context, WebsiteAlarm::class.java)
        val data: Uri = Uri.parse(intentAlarm.toUri(Intent.URI_INTENT_SCHEME))
        intentAlarm.data = data
        val pendingIntentAlarm = PendingIntent.getBroadcast(
            context, 1,
            intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val minute = theSecond * 60

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