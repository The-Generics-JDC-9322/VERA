package edu.gatech.vera.vera.model

import kotlinx.serialization.Serializable

@Serializable
data class HealthFeatureGeometry(val coordinates: DoubleArray?) {

    val type = "Point"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HealthFeatureGeometry

        if (coordinates != null) {
            if (!coordinates.contentEquals(other.coordinates!!)) return false
        } else {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        if (coordinates != null) {
            return coordinates.contentHashCode()
        }
        return 0
    }

}