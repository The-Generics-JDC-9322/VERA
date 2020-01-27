package edu.gatech.vera.vera.model.device

import edu.gatech.vera.vera.model.HealthData

interface WearableDevice {

    fun pauseMonitoring()

    fun endConnection()

    fun getHealthData(): HealthData

    fun getStatus(): DeviceStatus

    fun getInfo(): DeviceInfo

}