package com.sameerasw.essentials.services

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.sameerasw.essentials.MapsState

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName == "com.google.android.apps.maps") {
            MapsState.hasNavigationNotification = isNavigationNotification(sbn)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        if (sbn.packageName == "com.google.android.apps.maps") {
            MapsState.hasNavigationNotification = false
        }
    }

    private fun isNavigationNotification(sbn: StatusBarNotification): Boolean {
        val notification = sbn.notification
        if (!isPersistentNotification(notification)) return false
        return hasNavigationCategory(notification)
    }

    private fun isPersistentNotification(notification: Notification): Boolean {
        return (notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
    }

    private fun hasNavigationCategory(notification: Notification): Boolean {
        val category = notification.category ?: return false
        val navigationRegex = Regex("(?i).*navigation.*")
        return navigationRegex.containsMatchIn(category)
    }
}