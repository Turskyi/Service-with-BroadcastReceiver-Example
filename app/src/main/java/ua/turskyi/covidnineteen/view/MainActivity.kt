package ua.turskyi.covidnineteen.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ua.turskyi.covidnineteen.R
import ua.turskyi.covidnineteen.service.common.AlarmStarterService

/**
 * @Description
 * Every 10 minutes the app opens a browser with an arbitrary website, and every 20 minutes sends a
 * push notification with arbitrary text by clicking on which opens an arbitrary website.
 * After restarting the phone app should work as before.
 * When open the application should be displayed a blank screen.
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, AlarmStarterService::class.java))
    }
}
