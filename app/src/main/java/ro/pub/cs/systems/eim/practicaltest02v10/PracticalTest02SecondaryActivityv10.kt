package ro.pub.cs.systems.eim.practicaltest02v10

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val CHANNEL_ID = "pokemon_notifications"
        private const val CHANNEL_NAME = "Pokemon Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications for Pokémon updates"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Message received: ${remoteMessage.data}")

        val title = remoteMessage.notification?.title ?: "New Notification"
        val body = remoteMessage.notification?.body ?: "You have a new message"

        showNotification(title, body)
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, body: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }


}


class PracticalTest02SecondaryActivityv10 : AppCompatActivity() {
    private lateinit var subscribeText: EditText
    private lateinit var subscribeBtn: Button
    private lateinit var unsubscribeText: EditText
    private lateinit var unsubscribeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v10_secondary)

        subscribeText = findViewById(R.id.subscribeText)
        subscribeBtn = findViewById(R.id.subscribeBtn)
        unsubscribeText = findViewById(R.id.unsubscribeText)
        unsubscribeBtn = findViewById(R.id.unsubscribeBtn)

        subscribeBtn.setOnClickListener {
            val topic = subscribeText.text.toString()
            if (topic.isNotEmpty()) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Subscribed to $topic", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to subscribe to $topic", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a topic!", Toast.LENGTH_SHORT).show()
            }
        }

        unsubscribeBtn.setOnClickListener {
            val topic = unsubscribeText.text.toString()
            if (topic.isNotEmpty()) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Unsubscribed from $topic", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to unsubscribe from $topic", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a topic!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}