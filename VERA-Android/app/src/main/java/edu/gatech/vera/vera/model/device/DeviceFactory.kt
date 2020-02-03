package edu.gatech.vera.vera.model.device

import edu.gatech.vera.vera.model.Monitor
import edu.gatech.vera.vera.model.device.devices.NullDevice
import kotlin.reflect.KClass

/**
 * The DeviceFactory is a Factory object for building the WearableDevice
 * interface.
 *
 * Example:
 *
 *     //Build a FitbitLocalhostDevice and set the Monitor's device to it
 *     DeviceFactory.new()
 *         .ofType(FitbitLocalhostDevice::class)
 *         .build()
 *
 * @see WearableDevice
 * @see DeviceBuilder
 * @see Monitor
 */
object DeviceFactory {

    fun new(): DeviceBuilder {
        return DeviceBuilder()
    }

    /**
     * The DeviceBuilder class is a worker class for the DeviceFactory that
     * performs the work of building a WearableDevice.
     */
    class DeviceBuilder {

        /** The device that is being built */
        var device: WearableDevice = NullDevice()

        /**
         * Used to name the device.
         *
         * @param name the name to name the device.
         * @return the DeviceBuilder
         */
        fun named(name: String): DeviceBuilder {
            this.device.getInfo().name = name

            return this
        }

        /**
         * Used to set the WearableDevice's type.
         *
         * @param clazz the implementation class of WearableDevice.
         * @return the DeviceBuilder
         */
        fun ofType(clazz: Any): DeviceBuilder {
            val claz: KClass<WearableDevice> = clazz as KClass<WearableDevice>
            val newDevice: WearableDevice = claz.java.newInstance()
            //todo Maybe copy certain data that we set
            this.device = newDevice
            return this
        }

        /**
         * This function finalizes the build and sets the Monitor's device.
         */
        fun build() {
            Monitor.offerDevice(this.device)
        }
    }

}