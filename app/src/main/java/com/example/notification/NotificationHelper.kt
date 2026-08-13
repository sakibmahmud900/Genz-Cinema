package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.model.Movie
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

object NotificationHelper {

    private const val CHANNEL_ID = "genz_cinema_movies"
    private const val CHANNEL_NAME = "New Movie Uploads"
    private const val CHANNEL_DESC = "Notifications for newly uploaded movies and series"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendMovieNotification(context: Context, movieTitle: String, genre: String = "Action") {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIconBitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_notification_icon_1786563936286
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon_1786563936286)
            .setLargeIcon(largeIconBitmap)
            .setContentTitle("🎬 GenZ Cinema - New Movie Uploaded!")
            .setContentText("🔥 $movieTitle is now available to stream in 4K!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🎬 $movieTitle ($genre) has just been added by Admin. Open GenZ Cinema to watch now!")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Real-time listener: Listen to Firestore collection so every user gets notified instantly when Admin uploads a movie
    fun startListeningForNewMovies(context: Context) {
        val firestore = FirebaseFirestore.getInstance()
        var initialLoadCompleted = false

        firestore.collection("movies")
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) return@addSnapshotListener

                if (!initialLoadCompleted) {
                    initialLoadCompleted = true
                    return@addSnapshotListener
                }

                for (dc in snapshots.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val title = dc.document.getString("title") ?: "New Movie"
                        val genre = dc.document.getString("genre") ?: "Entertainment"
                        sendMovieNotification(context, title, genre)
                    }
                }
            }
    }
}
