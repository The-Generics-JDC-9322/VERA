package edu.gatech.vera.vera.model

import kotlinx.serialization.Serializable

@Serializable
data class HealthFeatureGeometry(val coordinates: IntArray) {

    val type = "Point"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HealthFeatureGeometry

        if (!coordinates.contentEquals(other.coordinates)) return false

        return true
    }

    override fun hashCode(): Int {
        return coordinates.contentHashCode()
    }

}