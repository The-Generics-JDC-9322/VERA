package edu.gatech.vera.vera.model

import android.os.Looper
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass

class AndroidMonitorTest {

    @Test
    fun setHealthData() {
        Looper.prepare()

        val newVal = -35
        val oldVal = 0
        var monitorListenerVal = oldVal

        assertEquals(Monitor.healthData, HealthData(0,0))

        Monitor.listener = object : HealthDataListener {
            override fun onVariableChanged(value: HealthData) {
                monitorListenerVal = newVal
            }

        }

        assertEquals(monitorListenerVal, oldVal)

        Monitor.healthData = HealthData(0, 0)

        assertEquals(Monitor.healthData, HealthData(0,0))
        assertEquals(monitorListenerVal, newVal)
    }

    @Test
    fun update() {

        val aData = HealthData(30, 12)
        Monitor.healthData = aData

        assertEquals(Monitor.healthData, aData)

        Monitor.update()

        assertEquals(Monitor.healthData, HealthData(0,0))
    }
}