package edu.gatech.vera.vera.model

import kotlinx.serialization.Serializable

/**
 * This class contains the Health Data for transportation as a GeoJSON. The
 * transportation to the Relay Transit Service happens from the Monitor.
 *
 * @see HealthData
 * @see Monitor
 *
 * @param timestamp time the data was taken
 * @param bpm heart rate in beats per min
 * @param badgeNumber the officer's badge number
 * @param officerName the officer's name
 */
@Serializable
data class HealthGeoJsonProperties(val timestamp: String,
                                   val bpm: Int,
                                   val badgeNumber: String,
                                   val officerName: String) {

    /** The status of the health data */
    val status = processBpm(bpm).string

    /**
     * Processes health data and classifies it using thresholds defined by the
     * ```HealthStatus``` enum. This method will return the highest status that
     * the bpm falls under.
     *
     * e.g.
     *
     *     var status = processBpm(-1)
     *
     *     assertEquals(HealthStatus, HealthStatus.NO_MEASUREMENT)
     *     
     *     status = processBpm(101)
     *
     *     assertEquals(HealthStatus, HealthStatus.ELEVATED)
     *
     * @param bpm
     * @return the health status
     */
    private fun processBpm(bpm: Int): HealthStatus {
        for (status in HealthStatus.values()) {
            if (bpm < status.threshold) return status
        }
        return HealthStatus.NO_MEASUREMENT
    }

}