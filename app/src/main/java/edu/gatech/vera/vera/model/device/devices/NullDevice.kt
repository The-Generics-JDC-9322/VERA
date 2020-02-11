package edu.gatech.vera.vera.model.device.devices

import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.device.DeviceInfo
import edu.gatech.vera.vera.model.device.DeviceStatus

class NullDevice : WearableDevice {
    override fun pauseMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getHealthData(): HealthData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getStatus(): DeviceStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfo(): DeviceInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}