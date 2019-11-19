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
    val delay : Long = 100
    val handler: Handler = Handler()



    fun offerDevice(device: WearableDevice) {
        this.fitbit = device
    }

    fun update() {
        Log.v("Debug", "Updating")



        handler.postDelayed(Runnable {
            update()
        }, delay)
    }


}