package com.kamel.limspeed

import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import com.kamel.limspeed.databinding.ActivityMainBinding
import com.kamel.limspeed.models.AppSpeed

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppListAdapter
    private val appSpeeds = mutableListOf<AppSpeed>()
    private var serviceIntent: Intent? = null
    private var globalSpeedKbps = 250

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        checkPermissions()
        loadApps()
    }

    private fun setupUI() {
        binding.apply {
            // Global speed slider
            sliderSpeed.addOnChangeListener { _, value, _ ->
                globalSpeedKbps = value.toInt()
                tvGlobalSpeed.text = "$globalSpeedKbps ${getString(R.string.speed_kb)}"
            }

            btnStart.setOnClickListener { startLimiting() }
            btnStop.setOnClickListener { stopLimiting() }

            // RecyclerView for apps
            adapter = AppListAdapter(appSpeeds) { appSpeed ->
                showSpeedDialog(appSpeed)
            }
            rvApps.layoutManager = LinearLayoutManager(this@MainActivity)
            rvApps.adapter = adapter
        }
    }

    private fun loadApps() {
        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        appSpeeds.clear()
        apps.forEach { app ->
            if (pm.getLaunchIntentForPackage(app.packageName) != null) {
                appSpeeds.add(
                    AppSpeed(
                        packageName = app.packageName,
                        appName = pm.getApplicationLabel(app).toString(),
                        icon = app.loadIcon(pm),
                        speedKbps = 0 // 0 يعني غير محدد
                    )
                )
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun showSpeedDialog(appSpeed: AppSpeed) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_speed, null)
        val slider = dialogView.findViewById<Slider>(R.id.sliderAppSpeed)
        val tvSpeed = dialogView.findViewById<TextView>(R.id.tvAppSpeed)

        slider.addOnChangeListener { _, value, _ ->
            val speed = value.toInt()
            tvSpeed.text = "$speed ${getString(R.string.speed_kb)}"
        }

        AlertDialog.Builder(this)
            .setTitle("${appSpeed.appName}")
            .setView(dialogView)
            .setPositiveButton(R.string.set_speed) { _, _ ->
                appSpeed.speedKbps = slider.value.toInt()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "${appSpeed.appName}: ${appSpeed.speedKbps} KB/s", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(intent)
            }
        }

        // Usage Stats Permission
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }

        if (mode != AppOpsManager.MODE_ALLOWED) {
            AlertDialog.Builder(this)
                .setTitle(R.string.permission_required)
                .setMessage("Please allow usage stats permission")
                .setPositiveButton("Open Settings") { _, _ ->
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    private fun startLimiting() {
        serviceIntent = Intent(this, SpeedLimiterService::class.java)
        serviceIntent?.putExtra("GLOBAL_SPEED", globalSpeedKbps)
        serviceIntent?.putExtra("APP_SPEEDS", HashMap<String, Int>().apply {
            appSpeeds.filter { it.speedKbps > 0 }.forEach {
                put(it.packageName, it.speedKbps)
            }
        })

        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 100)
        } else {
            startService(serviceIntent)
            binding.btnStart.isEnabled = false
            binding.btnStop.isEnabled = true
            Toast.makeText(this, R.string.start_service, Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopLimiting() {
        serviceIntent?.let {
            stopService(it)
            binding.btnStart.isEnabled = true
            binding.btnStop.isEnabled = false
            Toast.makeText(this, R.string.stop_service, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                startService(serviceIntent)
                binding.btnStart.isEnabled = false
                binding.btnStop.isEnabled = true
            }
        }
    }
}
