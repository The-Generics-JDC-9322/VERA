package edu.gatech.vera.vera.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Assert.*
import org.junit.Test

class HealthDataTest {


    @Test
    fun TestSerialization() {
        val json = Json(JsonConfiguration.Stable.copy())

        var serialized = json.stringify(HealthData.serializer(), HealthData(80))

        var expected = """{
            |"type":"Feature",
            |"geometry":{
                |"coordinates":[-Inf,-Inf],
                |"type":"Point",
            |},
            |"properties":{
                |"timestamp":sometime,
                |"bpm":80,
                |"badgeNumber":"",
                |"officerName":"",
            |},
            |}""".trimMargin()

        assertEquals(expected, serialized)

    }
}