package edu.gatech.vera.vera.model

import android.os.Looper
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass

class MonitorTest {

    @Test
    fun setHealthData() {

        val newVal = -35
        val oldVal = 0
        var monitorListenerVal = oldVal

        assertEquals(Monitor.healthData, HealthData(0))

        Monitor.listener =  {
                monitorListenerVal = it.bpm
        }

        assertEquals(monitorListenerVal, oldVal)

        Monitor.healthData = HealthData(0)

        assertEquals(Monitor.healthData, HealthData(0))
        assertEquals(monitorListenerVal, newVal)
    }

    @Test
    fun update() {

        val aData = HealthData(30)
        Monitor.healthData = aData

        assertEquals(Monitor.healthData, aData)

        Monitor.update()

        assertEquals(Monitor.healthData, HealthData(0))
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            Looper.prepare()
        }
    }
}