package edu.gatech.vera.vera.model

import android.os.Handler
import android.util.Log
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.device.devices.NullDevice

object Monitor {
    init {
        var handler = Handler()
        handler.postDelayed(Runnable {
            update()
        }, 500)
    }

    private var fitbit: WearableDevice = NullDevice()
    private const val delay : Long = 5000
    private val handler: Handler = Handler()
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

//        healthData = fitbit.getHealthData()
//        Log.d("fitbit", fitbit.getHealthData().toString())

        handler.postDelayed(Runnable {
            update()
        }, delay)
    }


}