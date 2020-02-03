package edu.gatech.vera.vera.model.device.devices

import android.app.Application
import com.tinder.scarlet.Message
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import edu.gatech.vera.vera.model.HealthData
import edu.gatech.vera.vera.model.device.WearableDevice
import edu.gatech.vera.vera.model.util.localhost.WebSocketRequest
import io.reactivex.Flowable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FitbitLocalhostDeviceTest {

    companion object {
        const val heartBeatValue1 = 87
        const val heartBeatValue2 = 122

        var fitbit: WearableDevice = NullDevice()

        @BeforeClass
        @JvmStatic
        fun init() {
            System.out.println("Called Before")
            fitbit = FitbitLocalhostDevice()
            (fitbit as FitbitLocalhostDevice).init()
        }
    }

    var versaLite = MockVersaLite()

    class MockVersaLite {
        val app = Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication").invoke(null) as Application

        val logging = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder().addInterceptor(logging)
            .build()
        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory("ws://127.0.0.1:4500"))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

        var webService: TestWebService? = null

        var connected = false

        var reqNum = 0


        fun streamData() {
            webService = scarletInstance.create<TestWebService>()

            webService?.observeWebSocketEvent()
                ?.subscribe {
                when (it) {
                    is WebSocket.Event.OnConnectionClosed -> {
                        System.out.println("Closed")
                        connected = false
                    }
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        webService?.sendText("[c]")
                        connected = true

                    }

                    is WebSocket.Event.OnMessageReceived -> {
                        System.out.println("Got Message")
                        System.out.println(it.message.toString())
                        val msg: String = (it.message as Message.Text).value
                        if (WebSocketRequest.getType(msg) == WebSocketRequest.GetHealthData) {
                            System.out.println("sending hb")
                            when (reqNum) {
                                0 -> webService?.sendText("[hb, $heartBeatValue1]")
                                1 -> webService?.sendText("[hb, $heartBeatValue2]")

                            }
                            reqNum++

                        }
                    }
                    is WebSocket.Event.OnConnectionClosing -> System.out.println("Closing")
                    is WebSocket.Event.OnConnectionFailed -> {
                        connected = false
                    }
                }
            }
        }

        fun kill() {
            webService?.sendText("[x]")
        }

        fun isConnected(): Boolean {
            return this.connected
        }
    }

    @Before
    fun startVera() {
        System.out.println("Calling Before")

        versaLite.streamData()
    }

    @After
    fun killClient() {
        System.out.println("Calling After")
        versaLite.kill()
    }

    @Test
    fun pauseMonitoring() {
        throw NotImplementedError()
    }

    @Test
    fun getHealthData() {

        GlobalScope.launch {
            assertEquals(HealthData(heartBeatValue1), fitbit.getHealthData())

        }

    }

    @Test fun getMultipleHealthData() {

        GlobalScope.launch {
            assertEquals(HealthData(heartBeatValue1), fitbit.getHealthData())
            assertEquals(HealthData(heartBeatValue2), fitbit.getHealthData())

        }
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

    interface TestWebService {
        @Receive
        fun observeWebSocketEvent() : Flowable<WebSocket.Event>

        @Send
        fun sendBytes(message: ByteArray)

        @Send
        fun sendText(message: String)
    }

}