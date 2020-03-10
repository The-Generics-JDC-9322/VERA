package edu.gatech.vera.vera.model.device.devices

import android.app.Application
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Message
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.LifecycleRegistry
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
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.lang.Thread.sleep

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
            sleep(500)
        }

        @AfterClass
        @JvmStatic
        fun killServer() {
            System.out.println("Called After FitbitLocalhostDeviceTest Class")
            (fitbit as FitbitLocalhostDevice).destroy()

        }
    }

    var versaLite = MockVersaLite()

    class MockVersaLite {
        val app = Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication").invoke(null) as Application

        val logging = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder().addInterceptor(logging)
            .build()

        val myCustomLifecycle = CustomLifecycle()
        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory("ws://127.0.0.1:4500"))
            .lifecycle(myCustomLifecycle)
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

        var webService = scarletInstance.create<TestWebService>()

        var connected = false

        var reqNum = 0


        fun streamData() {

            //scarletInstance.create<TestWebService>()
            myCustomLifecycle.lifecycleRegistry.onNext(Lifecycle.State.Started)

            webService.observeWebSocketEvent().subscribe {
                when (it) {
                    is WebSocket.Event.OnConnectionClosed -> {
                        System.out.println("Closed")
                        connected = false

                    }
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        webService.sendText("[c]")
                        connected = true
                    }

                    is WebSocket.Event.OnMessageReceived -> {
                        System.out.println("Got Message")
                        System.out.println(it.message.toString())
                        val msg: String = (it.message as Message.Text).value
                        if (WebSocketRequest.getType(msg) == WebSocketRequest.GetHealthData) {
                            System.out.println("sending hb")
                            when (reqNum) {
                                0 -> webService.sendText("[hb, $heartBeatValue1]")
                                1 -> webService.sendText("[hb, $heartBeatValue2]")

                            }
                            reqNum++

                        } else if (WebSocketRequest.getType(msg) == WebSocketRequest.EndConnection) {
                            connected = false
                            myCustomLifecycle.lifecycleRegistry.onNext(
                                Lifecycle.State.Stopped.WithReason(
                                    ShutdownReason.GRACEFUL))
                        }
                    }
                    is WebSocket.Event.OnConnectionClosing -> System.out.println("Closing")
                    is WebSocket.Event.OnConnectionFailed -> {
                        println(it.throwable.message)
                        println(it.throwable.printStackTrace())
                        connected = false

                    }
                }
            }
        }

        fun kill() {
            webService.sendText("[x]")
        }

        fun isConnected(): Boolean {
            return this.connected
        }
    }

    class CustomLifecycle(
        val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry() )
    : Lifecycle by lifecycleRegistry {

        init { lifecycleRegistry.onNext(Lifecycle.State.Started) }
    }

    @Before
    fun startVera() {
        System.out.println("Calling Before")


        GlobalScope.launch {
            versaLite.streamData()
        }

        while (!versaLite.isConnected()) {
            sleep(300)
        }

    }

    @After
    fun killClient() {
        System.out.println("Calling After")
        versaLite.kill()

        while (versaLite.isConnected()) {
            sleep(100)
        }
        println("Disconnected versaLite")

    }

//    @Test
//    fun pauseMonitoring() {
//        //TODO
//        throw NotImplementedError()
//    }

    @Test
    fun getHealthData() = runBlocking {

        val job1 = GlobalScope.launch {
            assertEquals(HealthData(heartBeatValue1), fitbit.getHealthData())
        }

        job1.join()


    }

    @Test fun getMultipleHealthData() = runBlocking {

        System.out.println("before runBlocking")

        val job1 = GlobalScope.launch {
            assertEquals(HealthData(heartBeatValue1), fitbit.getHealthData())
        }

        job1.join()

        val job2 = GlobalScope.launch {
            System.out.println("running Second block")
            assertEquals(HealthData(heartBeatValue2), fitbit.getHealthData())
        }

        job2.join()

        System.out.println("runBlocking Finished")
    }

//    @Test
//    fun getStatus() {
//        //TODO
//        throw NotImplementedError()
//    }
//
//    @Test
//    fun getInfo() {
//        //TODO
//        throw NotImplementedError()
//
//    }

    @Test
    fun endConnection() {
        System.out.println("Testing endConnection")
        GlobalScope.launch {
            fitbit.endConnection()
        }
        sleep(1200)
        println(versaLite.isConnected())
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