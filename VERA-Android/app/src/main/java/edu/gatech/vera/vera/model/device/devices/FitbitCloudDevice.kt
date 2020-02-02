package edu.gatech.vera.vera.model.device.devices

import android.util.Log
import edu.gatech.vera.vera.model.util.net.FitbitWebAPIClient
import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.device.DeviceInfo
import edu.gatech.vera.vera.model.device.DeviceStatus

class FitbitCloudDevice : WearableDevice {
    constructor() {
        // FitbitWebAPIClient.subscribeToUpdates(this)
    }

    var lastHealthData: HealthData = HealthData(0, 0)
    var deviceInfo: DeviceInfo = DeviceInfo("")




    override fun pauseMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getHealthData(): HealthData {
        // TODO check how old data is
        FitbitWebAPIClient.getHealthData(this)
        Log.d("in FitbitCloudDevice", lastHealthData.toString())
        return lastHealthData
    }

    override suspend fun getStatus(): DeviceStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfo(): DeviceInfo {
        return this.deviceInfo
    }

}