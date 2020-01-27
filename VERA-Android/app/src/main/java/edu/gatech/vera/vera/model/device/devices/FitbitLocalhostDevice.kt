package edu.gatech.vera.vera.model.device.devices

import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.device.DeviceInfo
import edu.gatech.vera.vera.model.device.DeviceStatus
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.util.localhost.WebSocketRequest
import edu.gatech.vera.vera.model.util.localhost.WebSocketServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FitbitLocalhostDevice : WearableDevice {

    var websocket: WebSocketServer = WebSocketServer()

    fun init() {
        //todo probably need to launch this in its own thread
        GlobalScope.launch {
            websocket.connect()
        }
    }

    override fun pauseMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endConnection() {
       websocket.request = WebSocketRequest.EndConnection
    }

    override fun getHealthData(): HealthData {
        websocket.request = WebSocketRequest.GetHealthData
        //todo wait for websocket to retrieve the info
        return HealthData(0,0)
    }

    override fun getStatus(): DeviceStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfo(): DeviceInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}