package edu.gatech.vera.vera.model

import android.location.Location
import android.os.SystemClock
import io.ktor.http.httpDateFormat
import kotlinx.serialization.Serializable
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Calendar

/**
 * This implements a Feature object according to [RFC7946](https://tools.ietf.o
 * rg/html/rfc7946#section-3.2). It is used to transmit health data to the
 * Relay Transit Service.
 *
 * e.g.
 *
 *     var jsonObj = json.stringify(HealthData.serializer(), HealthData(80))
 *
 *     var expected = `{`
 *                         type: "Feature"
 *
 *                  + `}`
 *     assertEquals("{a:1, b:42}", )
 *
 */
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