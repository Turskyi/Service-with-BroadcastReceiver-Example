package ua.turskyi.covidnineteen.service.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ua.turskyi.covidnineteen.service.pushnotification.NotificationReceiver
import ua.turskyi.covidnineteen.service.websiteopener.WebsiteAlarm
import ua.turskyi.covidnineteen.util.doNotKillApp
import ua.turskyi.covidnineteen.util.setNotificationInterval

class AfterRebootReceiver : BroadcastReceiver() {
    companion object {
        private const val UNLOCK = "unlock=============="
    }

    private val websiteAlarm: WebsiteAlarm = WebsiteAlarm()
    private val notificationAlarm: NotificationReceiver = NotificationReceiver()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action?.equals(Intent.ACTION_BOOT_COMPLETED) == true) {
            Toast.makeText(context, "ACTION_BOOT_COMPLETED ", Toast.LENGTH_LONG).show()
            Log.d(UNLOCK, "ACTION_BOOT_COMPLETED ")
            context.setNotificationInterval()
            websiteAlarm.setAnAlarmInterval(context)
            context.doNotKillApp()
        }
    }
}
