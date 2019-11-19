package edu.gatech.vera.vera.model

import edu.gatech.vera.vera.model.devices.DeviceInfo
import edu.gatech.vera.vera.model.devices.DeviceStatus

interface WearableDevice {

    fun pauseMonitoring()

    fun endConnection()

    fun getHealthData(): HealthData

    fun getStatus(): DeviceStatus

    fun getInfo(): DeviceInfo

}