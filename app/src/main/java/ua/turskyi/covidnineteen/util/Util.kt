package ua.turskyi.covidnineteen.util

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import ua.turskyi.covidnineteen.service.common.AfterRebootReceiver
import ua.turskyi.covidnineteen.service.pushnotification.NotificationReceiver

/**
 * Method checks if the app is in the background or not.
 */
fun Context.appIsInBackground(): Boolean {
    var isInBackground = true
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as
            ActivityManager
    val runningProcesses =
        activityManager.runningAppProcesses
    for (processInfo: ActivityManager.RunningAppProcessInfo in runningProcesses) {
        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            for (activeProcess in processInfo.pkgList) {
                if (activeProcess == packageName) {
                    isInBackground = false
                }
            }
        }
    }
    return isInBackground
}

fun vibratePhone(context: Context) {
    val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(200)
    }
}

fun Context.doNotKillApp() {
    val receiver = ComponentName(this, AfterRebootReceiver::class.java)
    packageManager?.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}

fun Context.setNotificationInterval() {
    val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as
            AlarmManager
    val intentAlarm = Intent(this, NotificationReceiver::class.java)
    val data: Uri = Uri.parse(intentAlarm.toUri(Intent.URI_INTENT_SCHEME))
    intentAlarm.data = data
    val pendingIntentAlarm: PendingIntent = PendingIntent.getBroadcast(
        this, 2,
        intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val minute: Long = 1000L * 60
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