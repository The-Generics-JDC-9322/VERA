package edu.gatech.vera.vera.model

import android.os.Looper
import edu.gatech.vera.vera.model.device.DeviceFactory
import edu.gatech.vera.vera.model.device.devices.FitbitLocalhostDevice
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import java.lang.Thread.sleep

class MonitorTest {

    @Test
    fun setHealthData() {

        val newVal = -35
        val oldVal = -1
        var monitorListenerVal = oldVal

        Monitor.healthData = HealthData(oldVal)

        assertEquals(HealthData(oldVal), Monitor.healthData)

        Monitor.listener =  {
                monitorListenerVal = it.bpm
        }

        assertEquals(monitorListenerVal, oldVal)

        Monitor.healthData = HealthData(newVal)

        assertEquals(HealthData(newVal), Monitor.healthData)
        assertEquals(monitorListenerVal, newVal)
    }

    @Test
    fun update() {

        val aData = HealthData(30)
        Monitor.healthData = aData

        assertEquals(aData, Monitor.healthData)

        Monitor.update()

        sleep(100)
        assertEquals(Monitor.healthData, HealthData(-1))
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            Looper.prepare()
        }
    }
}