package edu.gatech.vera.vera.model

import android.os.Handler
import android.util.Log
import edu.gatech.vera.vera.model.devices.NullDevice

object Monitor {
    init {
        var handler = Handler()
        handler.postDelayed(Runnable {
            update()
        }, 500)
    }

    var pullHealthData: Boolean = true
    var fitbit: WearableDevice = NullDevice()
    val delay : Long = 5000
    val handler: Handler = Handler()
    var healthData: HealthData = HealthData(0,0)
        set(value) {
            field = value
            this.listener?.onVariableChanged(value)
        }
    var listener: HealthDataListener? = null



    fun offerDevice(device: WearableDevice) {
        this.fitbit = device
    }

    fun update() {
        //Problem: When Update starts it never stops sending requests to the cloud for data. Even when the device is disconnected paused or even logged out!
        if (pullHealthData) {
            Log.v("Debug", "Updating")
            healthData = fitbit.getHealthData()
            Log.d("fitbit", fitbit.getHealthData().toString())
        }


        handler.postDelayed(Runnable {
            update()
        }, delay)
    }


}