package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ads.AdManager
import com.example.notification.NotificationHelper
import com.example.ui.MainAppScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // Initialize Notification Channels and Listeners
        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.startListeningForNewMovies(this)

        // Initialize Google Mobile Ads (AdMob)
        AdManager.initialize(this)

        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Show App Open Ad when coming to foreground
        AdManager.showAppOpenAdIfAvailable(this)
    }
}
