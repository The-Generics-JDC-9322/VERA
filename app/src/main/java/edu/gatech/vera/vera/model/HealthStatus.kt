package edu.gatech.vera.vera.model

enum class HealthStatus(val string: String, val threshold: Int) {
    NO_MEASURMENT("NO_MEASURMENT", 0),
    SEVERELY_LOW("SEVERELY_LOW", 40),
    LOW("LOW", 60),
    NORMAL("NORMAL", 100),
    ELEVATED("ELEVATED", 145),
    SEVERELY_HIGH("SEVERELY_HIGH", Int.MAX_VALUE)

}