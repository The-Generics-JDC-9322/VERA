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
 *     val json = Json(JsonConfiguration.Stable.copy())
 *
 *     var serialized = json.stringify(HealthData.serializer(), HealthData(80))
 *
 *     var expected = """{
 *         |"type":"Feature",
 *         |"geometry":{
 *             |"coordinates":null,
 *             |"type":"Point"
 *         |},
 *         |"properties":{
 *             |"timestamp":"${Calendar.getInstance().time}",
 *             |"bpm":80,
 *             |"badgeNumber":"",
 *             |"officerName":"",
 *             |"status":"NORMAL"
 *         |}
 *         |}""".trimMargin().replace("\\n".toRegex(), "")
 *
 *     assertEquals(expected, serialized)
 *
 *
 * @param bpm beats per minute defaulted to -1
 */
@Serializable
data class HealthData(@Transient val bpm: Int = -1) {

    /** The type should always be "Feature" */
    val type: String = "Feature"

    /** The geometry representing a point where this health data was taken */
    val geometry = constructGeometry()

    /** The time the health data was taken */
    @Transient
    private var currentTime = Calendar.getInstance().time.toString()

    /** The properties of the GeoJSON containing the data */
    val properties = HealthGeoJsonProperties(
        currentTime,
        bpm,
        Badge.number,
        Officer.name
    )

    /**
     * This method returns the geometry. Invalid coordinates are returned as
     * null.
     *
     * @return the geometry with the coordinates
     */
    fun constructGeometry(): HealthFeatureGeometry? {

        val negativeInfArray = doubleArrayOf(Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY)

        if (LocationService.getCoordinates().contentEquals(negativeInfArray)) {
            return HealthFeatureGeometry(null)
        }
        return HealthFeatureGeometry(LocationService.getCoordinates())
    }
}
