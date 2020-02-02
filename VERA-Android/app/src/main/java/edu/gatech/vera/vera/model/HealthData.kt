package edu.gatech.vera.vera.model

import android.location.Location
import android.os.SystemClock
import java.sql.Timestamp

data class HealthData(val bpm: Int) {
    val location: Location? = null
    val timeStamp: Long = System.currentTimeMillis()

}

public interface HealthDataListener {

    fun onVariableChanged(value: HealthData)
}