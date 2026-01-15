package com.sameerasw.essentials.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.sameerasw.essentials.R
import com.sameerasw.essentials.MainActivity

class CaffeinateWakeLockService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "Caffeinate::WakeLock")
        wakeLock?.acquire()

        // Initial state - Always show notification for foreground service
        startForeground(2, createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock?.release()
        wakeLock = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "caffeinate_live",
            getString(R.string.feat_caffeinate_title),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.caffeinate_live_channel_desc)
            setShowBadge(false)
            setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
            setSound(null, null)
            enableVibration(false)
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): android.app.Notification {
        val stopIntent = Intent(this, CaffeinateWakeLockService::class.java).apply { action = "STOP" }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val mainIntent = Intent(this, MainActivity::class.java).apply { putExtra("feature", "Caffeinate") }
        val mainPendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val activeText = "∞"

        if (Build.VERSION.SDK_INT >= 35) {
            val builder = Notification.Builder(this, "caffeinate_live")
                .setSmallIcon(R.drawable.rounded_coffee_24)
                .setContentTitle(getString(R.string.caffeinate_notification_title))
                .setContentText(getString(R.string.caffeinate_notification_desc))
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(mainPendingIntent)
                .addAction(Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.rounded_stop_circle_24),
                    getString(R.string.action_stop), stopPendingIntent).build())

            try {
                val extras = android.os.Bundle()
                extras.putBoolean("android.requestPromotedOngoing", true)
                extras.putString("android.shortCriticalText", activeText)
                builder.addExtras(extras)
                
                Notification.Builder::class.java.getMethod("setRequestPromotedOngoing", Boolean::class.javaPrimitiveType)
                    .invoke(builder, true)
                Notification.Builder::class.java.getMethod("setShortCriticalText", CharSequence::class.java)
                    .invoke(builder, activeText)
            } catch (_: Throwable) {}

            return builder.build()
        }

        val builder = NotificationCompat.Builder(this, "caffeinate_live")
            .setSmallIcon(R.drawable.rounded_coffee_24)
            .setContentTitle(getString(R.string.caffeinate_notification_title))
            .setContentText(getString(R.string.caffeinate_notification_desc))
            .setOngoing(true)
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setContentIntent(mainPendingIntent)
            .addAction(R.drawable.rounded_stop_circle_24, getString(R.string.action_stop), stopPendingIntent)

        val extras = android.os.Bundle()
        extras.putBoolean("android.requestPromotedOngoing", true)
        extras.putString("android.shortCriticalText", activeText)
        builder.addExtras(extras)

        return builder.build()
    }
}
