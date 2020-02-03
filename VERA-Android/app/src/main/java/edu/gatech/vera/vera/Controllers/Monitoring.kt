package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import edu.gatech.vera.vera.R
import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.HealthDataListener
import edu.gatech.vera.vera.model.Monitor
import edu.gatech.vera.vera.model.device.DeviceFactory
import edu.gatech.vera.vera.model.device.devices.FitbitCloudDevice
import edu.gatech.vera.vera.model.device.devices.FitbitLocalhostDevice

class Monitoring : AppCompatActivity() {

    private var monitoring : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        val badgeNumber = intent.extras.getSerializable("badgeNumber")
        val fitbit = intent.extras.getSerializable("fitbit")
        val fitbitId = findViewById<TextView>(R.id.fitbitId)
        fitbitId.text = fitbit.toString()

        Log.d("MonitoringController","Monitoring $badgeNumber using $fitbit")

        setupButtons()
        startListeningForHealthData()
    }

    private fun startListeningForHealthData() {
        val bpmAmt = Monitor.healthData
        val bpm = findViewById<TextView>(R.id.bpm)

        //getting a warning from Android Studio here
        bpm.setText("${bpmAmt.bpm} bpm")

        //define listener to set the bpm field text
        Monitor.listener = {
                Log.d("value", it.toString())

                //getting a warning from Android Studio here
                bpm.setText("${it.bpm} bpm")

        }

        //Build the device
        DeviceFactory.new()
            .ofType(FitbitLocalhostDevice::class)
            .build()

        Monitor.update()
    }

    private fun setupButtons() {
        val logoutButton = findViewById<Button>(R.id.logout2)
        val monitoringButton = findViewById<Button>(R.id.monitoring)
        val analyticsButton = findViewById<Button>(R.id.analytics)
        val settingsButton = findViewById<Button>(R.id.settings)

        val toggleMonitoringButton = findViewById<Button>(R.id.toggleMonitoring)
        val disconnectButton = findViewById<Button>(R.id.disconnect)

        logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            builder.setMessage("Are you sure you want to disconnect your Fitbit and logout of VERA?")
            builder.setPositiveButton("Logout", { dialog, which ->
                dialog.dismiss()
                Monitor.endMonitoring()
                val intent = Intent(this, Startup::class.java)
                startActivity(intent)
            })
            builder.setNegativeButton("Cancel", { dialog, which ->
                dialog.dismiss()
            })

            builder.create().show()
        }

        monitoringButton.setOnClickListener {
            val toast = Toast.makeText(this, "You're already on the Monitoring page!", Toast.LENGTH_SHORT)
            toast.show()
        }

        analyticsButton.setOnClickListener {
            val toast = Toast.makeText(this, "Stay tuned for implementation!", Toast.LENGTH_SHORT)
            toast.show()
        }

        settingsButton.setOnClickListener {
            val toast = Toast.makeText(this, "Stay tuned for implementation!", Toast.LENGTH_SHORT)
            toast.show()
        }

        toggleMonitoringButton.setOnClickListener {
            val imgId: Int
            val recordingVisibility: Int
            if (monitoring) {
                monitoring = false
                toggleMonitoringButton.text = "Resume Monitoring"
                imgId = R.drawable.monitoring_play_icon
                recordingVisibility = View.INVISIBLE
            } else {
                monitoring = true
                toggleMonitoringButton.text = "Pause Monitoring"
                imgId = R.drawable.monitoring_pause_icon
                recordingVisibility = View.VISIBLE
            }

            toggleMonitoringButton.setCompoundDrawablesWithIntrinsicBounds(0, imgId, 0, 0)
            val recordingStatus = findViewById<TextView>(R.id.recording_status)
            recordingStatus.visibility = recordingVisibility
        }

        disconnectButton.setOnClickListener {
            // TODO: disconnect device
            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            builder.setMessage("Are you sure you want to disconnect your Fitbit?")
            builder.setPositiveButton("Disconnect", { dialog, which ->
                println("disconnecting")
                dialog.dismiss()
                Monitor.endMonitoring()
                val intent = Intent(this, SelectFitbit::class.java)
                startActivity(intent)
            })
            builder.setNegativeButton("Cancel", { dialog, which ->
                println("cancelling")
                dialog.dismiss()
            })

            builder.create().show()
        }
    }

    override fun onBackPressed() {
        Monitor.endMonitoring()
        val intent = Intent(this, Startup::class.java)
        startActivity(intent)
    }
}
