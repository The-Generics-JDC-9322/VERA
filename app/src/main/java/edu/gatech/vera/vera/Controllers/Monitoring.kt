package edu.gatech.vera.vera.Controllers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import org.json.JSONObject
import java.util.jar.Manifest


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

        Monitor.update(this)

    }

    /**
     * This function starts the location service and provides it with
     * location the last location found for processing.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationService() {

        //start LocationService
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var locationL: Location? = null
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : android.location.LocationListener {
            override fun onLocationChanged(p0: Location?) {
                if (p0 != null) {
                    locationL = p0
                    LocationService.processLocation(p0)
                }
                Log.d("Longitude", locationL!!.longitude.toString())
                Log.d("Latitude", locationL!!.latitude.toString())
            }
            override fun onProviderDisabled(p0: String?) {
//
            }

            override fun onProviderEnabled(p0: String?) {
//
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
//
            }
        })
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                // Got last known location. In some rare situations this can be null.
//                locationL = location
//                if (location != null) {
//                    LocationService.processLocation(location)
//                    Log.d("Location", "Also WORKING")
//                    Log.d("LocationGPS", location.toString())
//                }
//            }
//        Log.d("Location", locationL.toString())
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

