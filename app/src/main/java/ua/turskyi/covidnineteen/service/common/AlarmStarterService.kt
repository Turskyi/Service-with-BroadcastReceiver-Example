package ua.turskyi.covidnineteen.service.common

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ua.turskyi.covidnineteen.service.pushnotification.NotificationAlarm
import ua.turskyi.covidnineteen.service.websiteopener.WebsiteAlarm

class AlarmStarterService : Service() {

    private val websiteAlarm = WebsiteAlarm()
    private val notificationAlarm = NotificationAlarm()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationAlarm.setAnAlarmInterval(this)
        websiteAlarm.setAnAlarmInterval(this)
        /*
       START_STICKY tells the OS to recreate the service after it has enough memory and call
       onStartCommand() again with a null intent. START_NOT_STICKY tells the OS to not bother
       recreating the service again.
       */
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}