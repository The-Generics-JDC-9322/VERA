package edu.gatech.vera.vera.model

/**
 * This enum class represents Health Statuses for alerts on the COP monitoring
 * platform. The thresholds are the upper bound on the status, so if a heart
 * rate is in between 40 and 59 (inclusive), it will be `HealthStatus.LOW`.
 * @param string A string description of the status.
 * @param threshold The upper bound on the category.
 * @see HealthGeoJsonProperties
 */
enum class HealthStatus(val string: String, val threshold: Int) {

    /**
     * NO_MEASUREMENT represents a health status for heartbeat values under 0.
     */
    NO_MEASUREMENT("NO_MEASUREMENT", 0),

    /**
     * SEVERELY_LOW represents a health status for heartbeat values from 0 to
     * 39 inclusive.
     */
    SEVERELY_LOW("SEVERELY_LOW", 40),

    /**
     * LOW represents a health status for heartbeat values from 40 to 59
     * inclusive.
     */
    LOW("LOW", 60),

    /**
     * NORMAL represents a health status for heartbeat values from 60 to 99
     * inclusive.
     */
    NORMAL("NORMAL", 100),

    /**
     * ELEVATED represents a health status for heartbeat values from 100 to
     * 144 inclusive.
     */
    ELEVATED("ELEVATED", 145),

    /**
     * SEVERELY_HIGH represents a health status for heartbeat values above 145
     * inclusive.
     */
    SEVERELY_HIGH("SEVERELY_HIGH", Int.MAX_VALUE)

}