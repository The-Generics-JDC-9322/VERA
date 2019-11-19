package edu.gatech.vera.vera.model.devices

import edu.gatech.vera.vera.model.FitbitWebAPIClient
import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.WearableDevice

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

    override fun getHealthData(): HealthData {
        // TODO check how old data is

        return FitbitWebAPIClient.getHealthData() //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStatus(): DeviceStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfo(): DeviceInfo {
        return this.deviceInfo
    }

}