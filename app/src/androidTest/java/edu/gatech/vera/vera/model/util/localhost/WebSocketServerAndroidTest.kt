package edu.gatech.vera.vera.model.util.localhost

import android.app.Application
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean


class WebSocketServerAndroidTest {

    /**
     * This test assures that we can connect to our webSocket Server using a
     * Kotlin WebSocket Client library. This will mimic the Javascript
     * WebSocket client that the Companion API uses. This test will send
     * messages as Strings.
     */
    @Test
    fun connectUsingScarletSendingText() {

        val app = Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication").invoke(null) as Application

        val logging = HttpLoggingInterceptor()
        logging.setLevel(Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .retryOnConnectionFailure(false)
            .build()

        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory("ws://127.0.0.1:4500"))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()


        val webService = scarletInstance.create<WebService>()

        val loop = AtomicBoolean(true)

        webService.observeWebSocketEvent()
            .subscribe {
                when (it) {
                    is WebSocket.Event.OnConnectionClosed -> {
                        System.out.println("Closed")
                        loop.set(false)
                    }
                    is WebSocket.Event.OnConnectionOpened<*> -> webService.sendText("[x]")

                    is WebSocket.Event.OnMessageReceived -> System.out.println("Got Message")
                    is WebSocket.Event.OnConnectionClosing -> System.out.println("Closing")
                    is WebSocket.Event.OnConnectionFailed -> System.out.println("Error")
                }
            }

        while (loop.get()) {
            webService.sendText("[c]")
        }
    }

    /**
     * This test assures that we can connect to our webSocket Server using a
     * Kotlin WebSocket Client library. This will mimic the Javascript
     * WebSocket client that the Companion API uses. This test will send
     * messages as Bytes.
     */
    @Test
    fun connectUsingScarletSendingBytes() {

        val app = Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication").invoke(null) as Application

        val logging = HttpLoggingInterceptor()
        logging.setLevel(Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging)
            .build()
        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory("ws://127.0.0.1:4500"))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(app))
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()


        val webService = scarletInstance.create<WebService>()

        val loop = AtomicBoolean(true)

        webService.observeWebSocketEvent()
            .subscribe {
                when (it) {
                    is WebSocket.Event.OnConnectionClosed -> {
                        System.out.println("Closed")
                        loop.set(false)
                    }
                    is WebSocket.Event.OnConnectionOpened<*> -> webService.sendBytes("[x]".toByteArray())

                    is WebSocket.Event.OnMessageReceived -> System.out.println("Got Message")
                    is WebSocket.Event.OnConnectionClosing -> System.out.println("Closing")
                    is WebSocket.Event.OnConnectionFailed -> System.out.println("Error")
                }

            }

        while (loop.get()) {
            webService.sendBytes("[c]".toByteArray())
        }

    }

    interface WebService {
        @Receive
        fun observeWebSocketEvent() : Flowable<WebSocket.Event>

        @Send
        fun sendBytes(message: ByteArray)

        @Send
        fun sendText(message: String)
    }

    companion object {

        /**
         * Method to start the WebSocketServer before running any tests.
         */
        @JvmStatic
        @BeforeClass
        fun startServer() {

            //Launch server
            WebSocketServer.connect()

        }

        /**
         * Method to kill the WebSocketServer after running any tests.
         */
        @JvmStatic
        @AfterClass
        fun killServer() {

            //Launch server
            WebSocketServer.terminate()

        }
    }


}