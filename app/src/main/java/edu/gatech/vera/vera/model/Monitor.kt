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
        Log.v("Debug", "Updating")

        healthData = fitbit.getHealthData()

        handler.postDelayed(Runnable {
            update()
        }, delay)
    }


}