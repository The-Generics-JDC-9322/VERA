package edu.gatech.vera.vera.Controllers

import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.gatech.vera.vera.R
import edu.gatech.vera.vera.model.Badge
import edu.gatech.vera.vera.model.LocationService
import edu.gatech.vera.vera.model.Monitor
import edu.gatech.vera.vera.model.Officer
import edu.gatech.vera.vera.model.device.DeviceFactory
import edu.gatech.vera.vera.model.device.devices.FitbitLocalhostDevice
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue
import android.support.v4.app.SupportActivity
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class Monitoring : AppCompatActivity() {

    private var monitoring : Boolean = true

    /** The FusedLocationClient for providing location data */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        val badgeNumber = intent?.extras?.getSerializable("badgeNumber")

        //set Badge.Number
        Badge.number = badgeNumber.toString()

        val fitbit = intent?.extras?.getSerializable("fitbit")
        val fitbitId = findViewById<TextView>(R.id.fitbitId)
        fitbitId.text = fitbit?.toString()

        Log.d("MonitoringController","Monitoring $badgeNumber using $fitbit")

        this.setupButtons()
        this.startListeningForHealthData()
        this.startLocationService()
    }

    /**
     * This function sets the HealthData listener and handles updating the bpm
     * display.
     */
    private fun startListeningForHealthData() {
        val bpm = findViewById<TextView>(R.id.bpm)
        val data = JSONObject("""{"geometry":{"type":"Point","coordinates":[-84.396431,33.778172]}, "properties":{"bpm":100}}""")
        val url = "https://aqueous-falls-60920.herokuapp.com/hb/5000"
        val request = JsonObjectRequest(Request.Method.POST,url,data,
            Response.Listener { response ->
                // Process the json
                Log.d("Success", response.toString())

            }, Response.ErrorListener{
                // Error in request
                Log.d("ERROR", "Post Request Error Response")
            })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
        Log.d("FINISHED", "!!!!!!!!!!!!!!!!!!")
        //define listener to set the bpm field text
        Monitor.listener = {
                Log.d("Monitoring value", it.toString())

                var bpmStr = "-- bpm"
                if (it.bpm >= 0) {
                    bpmStr = "${it.bpm} bpm"
                }

                bpm.text = bpmStr


        }

        //Build the device
        DeviceFactory.new()
            .ofType(FitbitLocalhostDevice::class)
            .build()

        Monitor.update()

    }

    /**
     * This function starts the location service and provides it with
     * location the last location found for processing.
     */
    private fun startLocationService() {

        //start LocationService
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    LocationService.processLocation(location)
                }
            }
    }

    private fun setupButtons() {
        val logoutButton = findViewById<Button>(R.id.logout2)
        val monitoringButton = findViewById<Button>(R.id.monitoring)
        val analyticsButton = findViewById<Button>(R.id.analytics)
        val settingsButton = findViewById<Button>(R.id.settings)

        val toggleMonitoringButton = findViewById<Button>(R.id.toggleMonitoring)
        val disconnectButton = findViewById<Button>(R.id.disconnect)

        logoutButton.setOnClickListener { logout() }

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
                val resumeMonitoringStr = "Resume Monitoring"
                toggleMonitoringButton.text = resumeMonitoringStr
                imgId = R.drawable.monitoring_play_icon
                recordingVisibility = View.INVISIBLE
            } else {
                monitoring = true
                val pauseMonitoringStr = "Pause Monitoring"
                toggleMonitoringButton.text = pauseMonitoringStr
                imgId = R.drawable.monitoring_pause_icon
                recordingVisibility = View.VISIBLE
            }

            toggleMonitoringButton.setCompoundDrawablesWithIntrinsicBounds(0, imgId, 0, 0)
            val recordingStatus = findViewById<TextView>(R.id.recording_status)
            recordingStatus.visibility = recordingVisibility
        }

        disconnectButton.setOnClickListener { logout() }
    }

    private fun logout() {
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

    override fun onBackPressed() {
        Monitor.endMonitoring()
        val intent = Intent(this, Startup::class.java)
        startActivity(intent)
    }
}
