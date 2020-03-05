package edu.gatech.vera.vera.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class HealthDataTest {


    @Test
    fun testSerialization() {
        val json = Json(JsonConfiguration.Stable.copy())

        var serialized = json.stringify(HealthData.serializer(), HealthData(80))

        var expected = """{
            |"type":"Feature",
            |"geometry":{
                |"coordinates":null,
                |"type":"Point"
            |},
            |"properties":{
                |"timestamp":"${Calendar.getInstance().time}",
                |"bpm":80,
                |"badgeNumber":"",
                |"officerName":"",
                |"status":"NORMAL"
            |}
            |}""".trimMargin().replace("\\n".toRegex(), "")

        assertEquals(expected, serialized)

    }
}