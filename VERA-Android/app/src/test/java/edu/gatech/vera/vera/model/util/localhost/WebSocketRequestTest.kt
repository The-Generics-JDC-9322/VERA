package edu.gatech.vera.vera.model.util.localhost

import org.junit.Assert.*
import org.junit.Test

class WebSocketRequestTest {


    @Test fun testGetType() {
        val getHeartBeat = WebSocketRequest.getType("[hb]")
        assertEquals(WebSocketRequest.GetHealthData, getHeartBeat)

        val getHeartBeat2 = WebSocketRequest.getType("[hb,1342]")
        assertEquals(WebSocketRequest.GetHealthData, getHeartBeat2)

        val getHeartBeat3 = WebSocketRequest.getType("[hb,]")
        assertEquals(WebSocketRequest.GetHealthData, getHeartBeat3)

        val connect = WebSocketRequest.getType("[c]")
        assertEquals(WebSocketRequest.ConnectRequest, connect)

        val connect1 = WebSocketRequest.getType("[c,]")
        assertEquals( WebSocketRequest.ConnectRequest, connect1)

        val end = WebSocketRequest.getType("[x]")
        assertEquals(WebSocketRequest.EndConnection, end)

        val end1 = WebSocketRequest.getType("[x,]")
        assertEquals(WebSocketRequest.EndConnection, end1)

        val none = WebSocketRequest.getType("")
        assertEquals(WebSocketRequest.NullRequest, none)

        val none1 = WebSocketRequest.getType("1")
        assertEquals(WebSocketRequest.NullRequest, none1)
    }
}