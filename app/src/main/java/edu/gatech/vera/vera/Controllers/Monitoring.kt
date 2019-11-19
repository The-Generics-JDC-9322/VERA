package edu.gatech.vera.vera.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import edu.gatech.vera.vera.R
import edu.gatech.vera.vera.model.devices.DeviceFactory
import edu.gatech.vera.vera.model.devices.FitbitCloudDevice
import kotlinx.android.synthetic.main.activity_monitoring.*

class Monitoring : AppCompatActivity() {

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
        val monitoringButton = findViewById<Button>(R.id.monitoring)
        val analyticsButton = findViewById<Button>(R.id.analytics)
        val settingsButton = findViewById<Button>(R.id.settings)

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
//            val recordingStatus = findViewById<TextView>(R.id.recording_status)
//            recordingStatus.visibility = View.VISIBLE
        }
    }
}
