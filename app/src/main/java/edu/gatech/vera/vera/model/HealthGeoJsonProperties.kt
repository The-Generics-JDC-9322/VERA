package edu.gatech.vera.vera.model

import kotlinx.serialization.Serializable

@Serializable
data class HealthGeoJsonProperties(val timestamp: String,
                                   val bpm: Int,
                                   val badgeNumber: String,
                                   val officerName: String) {
    val status = processBpm(bpm).string

    fun processBpm(bpm: Int): HealthStatus {
        for (status in HealthStatus.values()) {
            if (bpm < status.threshold) return status
        }
        return HealthStatus.NO_MEASURMENT
    }

}