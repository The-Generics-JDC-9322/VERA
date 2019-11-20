package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import edu.gatech.vera.vera.R
import edu.gatech.vera.vera.model.devices.DeviceFactory
import edu.gatech.vera.vera.model.devices.FitbitCloudDevice
import kotlinx.android.synthetic.main.activity_monitoring.*

class Monitoring : AppCompatActivity() {

    private var monitoring : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fitbit = intent.extras.getSerializable("fitbit")
        println("Monitoring $fitbit")
        setContentView(R.layout.activity_monitoring)

        val fitbitId = findViewById<TextView>(R.id.fitbitId)
        fitbitId.text = fitbit.toString()

        setupButtons()

        DeviceFactory.new()
            .ofType(FitbitCloudDevice::class)
            .named(fitbit.toString())
            .build()
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
}
