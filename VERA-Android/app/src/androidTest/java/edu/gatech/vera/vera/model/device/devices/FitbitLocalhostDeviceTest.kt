package edu.gatech.vera.vera.model.device.devices

import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.util.localhost.WebSocketServer
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import java.net.Socket
import java.util.concurrent.TimeUnit

class FitbitLocalhostDeviceTest {

    companion object {
        const val heartBeatValue = 87
        var fitbit: WearableDevice = NullDevice()


        @BeforeClass
        @JvmStatic
        fun init() {
            System.out.println("Called Before")
            fitbit = FitbitLocalhostDevice()
        }
    }

    var versaLite = MockVersaLite()

    class MockVersaLite {
        var clientSocket = Socket()

        fun streamData() {
            System.out.println(WebSocketServer.serverSocket.isClosed)
            clientSocket = Socket("localhost", 4500)
            System.out.println("Created Client Socket")
            assertTrue(clientSocket.isConnected)
            val out = clientSocket.getOutputStream()
            out.write("\r\n\r\nGET\r\n\r\n".toByteArray())
            out.write("[hb, $heartBeatValue]".toByteArray())
        }

        fun kill() {
            clientSocket.close()
        }

        fun isConnected(): Boolean {
            return clientSocket.isConnected
        }
    }

    @Before
    fun startVera() {
        System.out.println("Calling Before")
        (fitbit as FitbitLocalhostDevice).init()
        versaLite.streamData()
    }

    @After
    fun killClient() {
        System.out.println("Calling After")
        versaLite.kill()
        TimeUnit.SECONDS.sleep(1)
        System.out.println(WebSocketServer.serverSocket.isClosed)
    }

    @Test
    fun pauseMonitoring() {
        throw NotImplementedError()
    }

    @Test
    fun getHealthData() {
        assertEquals(fitbit.getHealthData(), HealthData(heartBeatValue, 0))
    }

    @Test
    fun getStatus() {
        throw NotImplementedError()
    }

    @Test
    fun getInfo() {
        throw NotImplementedError()

    }

    @Test
    fun endConnection() {
        System.out.println("Testing endConnection")
        fitbit.endConnection()
        assertFalse(versaLite.isConnected())
    }


}