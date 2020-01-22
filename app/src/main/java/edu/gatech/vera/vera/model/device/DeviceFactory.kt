package edu.gatech.vera.vera.model.device

import edu.gatech.vera.vera.model.Monitor
import edu.gatech.vera.vera.model.device.devices.NullDevice
import kotlin.reflect.KClass

object DeviceFactory {

    fun new(): DeviceBuilder {
        return DeviceBuilder()
    }

    class DeviceBuilder {
        var device: WearableDevice = NullDevice()


        fun named(name: String): DeviceBuilder {
            this.device.getInfo().name = name

            return this
        }

        fun ofType(clazz: Any): DeviceBuilder {
            val claz: KClass<WearableDevice> = clazz as KClass<WearableDevice>
            val newDevice: WearableDevice = claz.java.newInstance()
            //todo Maybe copy certain data that we set
            this.device = newDevice
            return this
        }

        fun build() {
            Monitor.offerDevice(this.device)
        }
    }

}