package edu.gatech.vera.vera.model

import android.content.Context
import android.os.Handler
import android.util.Log
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.device.devices.NullDevice
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


/**
 * This is a singleton Monitor class that handles the Monitoring of all data
 * to and from the WearableDevice connected to VERA. The main work of the class
 * is done by the update loop which also queues itself to run every delay ms.
 *
 * This class interacts with the views through the controller class
 * Monitoring.kt.
 *
 * @see WearableDevice
 * @see Monitoring
 */
object Monitor {

    /** The WearableDevice Monitor is monitoring */
    private var fitbit: WearableDevice = NullDevice()

    /** The delay in handler calls in milliseconds */
    private const val delay : Long = 5000

    /** The handler that runs the update loop */
    private val handler: Handler = Handler()

    /**
     * The current health data held by the Monitor. Custom setter notifies the
     * listener.
     */
    var healthData: HealthData = HealthData(-1)
        set(value) {
            field = value
            this.listener(value)
        }

    /** The listener that is called when HealthData is changed */
    var listener: ((value: HealthData) -> Unit) = {
        Log.d("Monitor", "listener needs to be implemented")
    }


    /**
     * This function is used by DeviceFactory to set the Monitor's wearable
     * device.
     *
     * @param device the built wearable device offered to the Monitor
     */
    fun offerDevice(device: WearableDevice) {
        this.fitbit = device
    }

    /**
     * Update loop requests health data from the WearableDevice every delay.
     * This function sets the Monitor's healthData variable.
     */
    fun update(context: Context) {

        GlobalScope.launch (Dispatchers.Main){
            healthData = fitbit.getHealthData()
            var bpm = healthData.bpm
            Log.d("BPM", healthData.bpm.toString())
            val json = Json(JsonConfiguration.Stable.copy())
            val data = JSONObject(json.stringify(HealthData.serializer(), HealthData(bpm)))
            val url = "https://aqueous-falls-60920.herokuapp.com/hb/" + Badge.number
            val request = JsonObjectRequest(Request.Method.POST,url,data,
                Response.Listener { response ->
                    // Process the json
                    Log.d("Success", response.toString())

                }, Response.ErrorListener{
                    // Error in request
                    Log.d("ERROR", "Post Request Error Response")
                })
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(request)
        }


        handler.postDelayed({
            update(context)
        }, delay)
    }

    /**
     * Ends the Monitoring by telling the WearableDevice to end the connection
     * and cancel the handler.
     */
    fun endMonitoring() {
        fitbit.endConnection()

        //remove all Callbacks and Messages from the handler
        handler.removeCallbacksAndMessages(null)
    }


}