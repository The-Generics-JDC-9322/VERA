package edu.gatech.vera.vera.model.devices

import edu.gatech.vera.vera.model.Monitor
import edu.gatech.vera.vera.model.WearableDevice
import kotlin.reflect.KClass
import kotlin.reflect.KType

object DeviceFactory {

    fun new(): DeviceBuilder {
        return DeviceBuilder()
    }

    class DeviceBuilder {
        var device: WearableDevice = NullDevice()


        fun named(name: String): DeviceBuilder {
            this.device.name = name

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