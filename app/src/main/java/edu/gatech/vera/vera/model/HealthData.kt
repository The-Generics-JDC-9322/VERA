package edu.gatech.vera.vera.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

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
data class HealthData(@Transient val bpm: Int = -1) {
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