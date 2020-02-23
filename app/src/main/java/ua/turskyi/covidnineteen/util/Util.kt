package ua.turskyi.covidnineteen.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Method checks if the app is in the background or not.
 */
fun appIsInBackground(context: Context): Boolean {
    var isInBackground = true
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as
            ActivityManager
    val runningProcesses =
        activityManager.runningAppProcesses
    for (processInfo in runningProcesses) {
        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            for (activeProcess in processInfo.pkgList) {
                if (activeProcess == context.packageName) {
                    isInBackground = false
                }
            }
        }
    }
    return isInBackground
}

fun vibratePhone(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(200)
    }
}