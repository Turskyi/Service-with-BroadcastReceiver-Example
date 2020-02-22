package ua.turskyi.covidnineteen.util

import android.app.ActivityManager
import android.content.Context

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