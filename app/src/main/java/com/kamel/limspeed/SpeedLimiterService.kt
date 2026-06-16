package com.kamel.limspeed

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import java.util.concurrent.ConcurrentHashMap

class SpeedLimiterService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null
    private var globalSpeedKbps = 250
    private val appSpeeds = ConcurrentHashMap<String, Int>()
    private var isRunning = false
    private val CHANNEL_ID = "LimSpeedService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        globalSpeedKbps = intent?.getIntExtra("GLOBAL_SPEED", 250) ?: 250
        val speeds = intent?.getSerializableExtra("APP_SPEEDS") as? HashMap<String, Int>
        speeds?.let { appSpeeds.putAll(it) }

        createNotificationChannel()
        startForeground(1, createNotification())

        setupVpn()
        return START_STICKY
    }

    private fun setupVpn() {
        val builder = Builder()
            .setSession("LimSpeed")
            .addAddress("10.0.0.1", 24)
            .addDnsServer("8.8.8.8")
            .addRoute("0.0.0.0", 0)

        vpnInterface = builder.establish()
        isRunning = true
        
        Thread {
            // VPN processing logic (Simplified for structure)
            while (isRunning) {
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun getAppFromPacket(packet: ByteArray): Pair<String, Int> {
        // Simplified - In real implementation, parse IP header to get UID
        // This is a placeholder
        return Pair("com.android.chrome", 1000)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Speed Limiter",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows speed limiting status"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("LimSpeed")
            .setContentText("Speed limit active: $globalSpeedKbps KB/s")
            .setSmallIcon(android.R.drawable.ic_menu_share)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        vpnInterface?.close()
        vpnInterface = null
    }
}
