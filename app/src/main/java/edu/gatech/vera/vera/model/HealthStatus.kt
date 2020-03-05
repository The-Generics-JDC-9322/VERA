package edu.gatech.vera.vera.model

/**
 * This enum class represents Health Statuses for alerts on the COP monitoring
 * platform. The thresholds are the upper bound on the status, so if a heart
 * rate is in between 40 and 59 (inclusive), it will be ```HealthStatus.LOW```.
 *
 * @see HealthGeoJsonProperties
 *
 * @param string this is a string description of the status
 * @param threshold this is the upper bound on the category
 */
enum class HealthStatus(val string: String, val threshold: Int) {
    NO_MEASUREMENT("NO_MEASUREMENT", 0),
    SEVERELY_LOW("SEVERELY_LOW", 40),
    LOW("LOW", 60),
    NORMAL("NORMAL", 100),
    ELEVATED("ELEVATED", 145),
    SEVERELY_HIGH("SEVERELY_HIGH", Int.MAX_VALUE)

}