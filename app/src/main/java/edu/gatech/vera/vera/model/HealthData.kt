package edu.gatech.vera.vera.model

import android.location.Location
import android.os.SystemClock
import io.ktor.http.httpDateFormat
import kotlinx.serialization.Serializable
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Calendar

@Serializable
data class HealthData(@Transient val bpm: Int) {
    val type: String = "Feature"
    val geometry = HealthFeatureGeometry(LocationService.getCoordinates())
    @Transient
    private var time = Calendar.getInstance().time.toString()
    val properties = HealthGeoJsonProperties(
        time,
        bpm,
        Badge.number,
        Officer.name
    )

}

interface HealthDataListener {

    fun onVariableChanged(value: HealthData)
}