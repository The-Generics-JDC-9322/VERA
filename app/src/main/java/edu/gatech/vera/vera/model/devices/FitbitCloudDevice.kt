package edu.gatech.vera.vera.model.devices

import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.WearableDevice

class FitbitCloudDevice : WearableDevice {

    var deviceInfo:DeviceInfo = DeviceInfo("")

    override fun pauseMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHealthData(): HealthData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStatus(): DeviceStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfo(): DeviceInfo {
        return this.deviceInfo
    }

}