package ua.turskyi.covidnineteen.service.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ua.turskyi.covidnineteen.service.pushnotification.NotificationAlarm
import ua.turskyi.covidnineteen.service.websiteopener.WebsiteAlarm

class AutoStartAlarm : BroadcastReceiver() {

    private val websiteAlarm = WebsiteAlarm()
    private val notificationAlarm = NotificationAlarm()

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action?.equals(Intent.ACTION_BOOT_COMPLETED) == true){
            notificationAlarm.setAnAlarmInterval(context)
            websiteAlarm.setAnAlarmInterval(context)
        }
    }
}
