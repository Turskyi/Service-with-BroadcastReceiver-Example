package ua.turskyi.covidnineteen.service.websiteopener

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import ua.turskyi.covidnineteen.R
import ua.turskyi.covidnineteen.util.vibratePhone

class WebsiteService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        openWebsite()
        /*
          START_STICKY tells the OS to recreate the service after it has enough memory and call
          onStartCommand() again with a null intent. START_NOT_STICKY tells the OS to not bother
          recreating the service again.
          */
        return START_NOT_STICKY
    }

    private fun openWebsite() {
        vibratePhone(applicationContext)
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_link)))
        val pendingIntentBrowser: PendingIntent = PendingIntent.getActivity(
            this, 0,
            intentBrowser, 0
        )
        pendingIntentBrowser.send()
    }
}