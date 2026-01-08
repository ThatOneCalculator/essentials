package com.sameerasw.essentials.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import com.sameerasw.essentials.utils.ShizukuUtils
import moe.shizuku.server.IShizukuService
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import java.lang.reflect.Method

object FreezeManager {
    private const val TAG = "FreezeManager"

    // Hidden API constants
    private const val COMPONENT_ENABLED_STATE_DEFAULT = 0
    private const val COMPONENT_ENABLED_STATE_ENABLED = 1
    private const val COMPONENT_ENABLED_STATE_DISABLED = 2
    private const val COMPONENT_ENABLED_STATE_DISABLED_USER = 3
    private const val COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4

    /**
     * Freeze an application using Shizuku.
     * Sets state to COMPONENT_ENABLED_STATE_DISABLED_USER (3).
     */
    fun freezeApp(packageName: String): Boolean {
        return setApplicationEnabledSetting(packageName, COMPONENT_ENABLED_STATE_DISABLED_USER)
    }

    /**
     * Unfreeze an application using Shizuku.
     * Sets state to COMPONENT_ENABLED_STATE_ENABLED (1).
     */
    fun unfreezeApp(packageName: String): Boolean {
        return setApplicationEnabledSetting(packageName, COMPONENT_ENABLED_STATE_ENABLED)
    }

    /**
     * Check if an application is currently frozen/disabled.
     */
    fun isAppFrozen(context: Context, packageName: String): Boolean {
        return try {
            val state = context.packageManager.getApplicationEnabledSetting(packageName)
            state == COMPONENT_ENABLED_STATE_DISABLED_USER || state == COMPONENT_ENABLED_STATE_DISABLED
        } catch (e: Exception) {
            false
        }
    }

    private fun setApplicationEnabledSetting(packageName: String, newState: Int): Boolean {
        if (!ShizukuUtils.isShizukuAvailable() || !ShizukuUtils.hasPermission()) {
            return false
        }

        return try {
            // Get the Package Manager service through Shizuku
            val service = ShizukuBinderWrapper(rikka.shizuku.SystemServiceHelper.getSystemService("package"))
            val pmClass = Class.forName("android.content.pm.IPackageManager")
            val stubClass = Class.forName("android.content.pm.IPackageManager\$Stub")
            val asInterfaceMethod = stubClass.getMethod("asInterface", IBinder::class.java)
            val ipm = asInterfaceMethod.invoke(null, service)

            // IPackageManager.setApplicationEnabledSetting(packageName, newState, flags, userId, callingPackage)
            // Note: Method signature varies across Android versions.
            // Standard: setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage)
            
            val method = ipm.javaClass.getMethod(
                "setApplicationEnabledSetting",
                String::class.java,
                Int::class.java,
                Int::class.java,
                Int::class.java,
                String::class.java
            )
            
            method.invoke(ipm, packageName, newState, 0, 0, "com.android.shell")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
