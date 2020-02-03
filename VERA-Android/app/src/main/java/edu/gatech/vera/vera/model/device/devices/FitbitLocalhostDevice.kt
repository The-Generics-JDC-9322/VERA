package edu.gatech.vera.vera.model.device.devices

import android.util.Log
import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.device.DeviceInfo
import edu.gatech.vera.vera.model.device.DeviceStatus
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.util.localhost.WebSocketRequest
import edu.gatech.vera.vera.model.util.localhost.WebSocketServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * The FitbitLocahostDevice class is the default implementation for the
 * WearableDevice Interface. The class represents a high-level abstraction of
 * the Fitbit Wearable Device running the Fitbit Companion API. It uses the
 * WebSocketServer object to handle low level communications with the Fitbit
 * Companion API.
 * <p>
 * The FitbitLocalhostDevice class's methods are called to perform requests to
 * the Fitbit Companion API running in the Fitbit Mobile App which then uses
 * a PeerSocket implementation to communicate with the Device API which can
 * read the wearable device sensors.
 *
 * @author navaem@gatech.edu
 * @see WearableDevice
 * @see WebSocketServer
 */
class FitbitLocalhostDevice : WearableDevice {

    /** Reference to the WebSocketServer object*/
    private val webSocket: WebSocketServer = WebSocketServer

    /**
     * Constructor calls the init() function.
     * */
    init {
        GlobalScope.launch {
            init()
        }
    }

    /**
     * This method calls the WebSocketServer's method to start the embedded
     * server. Calling this method multiple times will result in Java socket
     * binding exceptions.
     */
    fun init() {
        Log.d("FitbitLocalhostDevice", "before launch")
        webSocket.connect()

    }

    override fun pauseMonitoring() {
        TODO("not implemented")
    }

    /**
     * This method sends end connection messages to the client. The Companion
     * API will then gracefully close the socket and disconnect from the
     * WebSocketServer.
     */
    override fun endConnection() {
        GlobalScope.launch {
            webSocket.request(WebSocketRequest.EndConnection)
        }
    }

    /**
     * This is a suspending function which means it must be called from a
     * Kotlin CoroutineScope. It will launch an asynchronous request for
     * HealthData from the Companion API. It is possible that the request will
     * timeout or that no client is connected. In cases where the
     * FitbitLocalhostDevice cannot retrieve the HealthData from the Companion
     * API, it will return HealthData with a value of -1.
     *
     * @return the health data from the Companion API
     */
    override suspend fun getHealthData(): HealthData {
        //suspending function call to request
        val bpm = webSocket.request(WebSocketRequest.GetHealthData)

        return HealthData(bpm)
    }

    override suspend fun getStatus(): DeviceStatus {
        TODO("not implemented")
    }

    override fun getInfo(): DeviceInfo {
        TODO("not implemented")
    }

}