package edu.gatech.vera.vera.model.device

import edu.gatech.vera.vera.model.HealthData

/**
 * Interface that represents a WearableDevice that connects to VERA. This
 * interface is used to send commands and manipulate the physical wearable
 * device from VERA. Functions that require a response from the device are
 * suspending functions. The default implementation in VERA the
 * FitbitLocalhostDevice class.
 *
 * For more information about suspending functions and coroutines see
 * (Coroutines Guide)[https://kotlinlang.org/docs/reference/coroutines/coroutin
 * es-guide.html]
 *
 * @see FitbitLocalhostDevice
 */
interface WearableDevice {

    /**
     * Implement this function to pause the Health Data recording on the
     * physical wearable device
     */
    fun pauseMonitoring()

    /**
     * Implement this function to end VERA's connection with the physical
     * wearable device.
     */
    fun endConnection()

    /**
     * Implement this suspending function to allow VERA to request HealthData
     * from the physical wearable device.
     *
     * @return health data from the wearable device
     */
    suspend fun getHealthData(): HealthData

    /**
     * Implement this suspending function to allow VERA to request the
     * DeviceStatus from the physical wearable device.
     *
     * @return the device status from the wearable device
     */
    suspend fun getStatus(): DeviceStatus

    /**
     * Implement this function to provide VERA with information about the
     * physical wearable device.
     *
     * @return the device info from the implementation
     */
    fun getInfo(): DeviceInfo

}