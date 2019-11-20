package edu.gatech.vera.vera.model.util.net

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import edu.gatech.vera.vera.model.HealthData
import org.json.JSONObject
import java.io.File

object FitbitWebAPIClient {

    val cacheDir = File("~/.VERA/cache")

    val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

    val network = BasicNetwork(HurlStack())

    val requestQueue = RequestQueue(
        cache,
        network
    ).apply {
        start()
    }

    val url = "https://api.fitbit.com"

    val tokenRequestPath = "oauth2/token"


    var token: Any? = null

   // var actual_token: Any? = "{"access_token":"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMkJGQzciLCJzdWIiOiI3UE1NRzciLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyYWN0IHJociBycHJvIiwiZXhwIjoxNTc0MjU0MTA3LCJpYXQiOjE1NzQyMjUzMDd9.kIM6FVdnra2vHD5kCUaUlK4_NOrqwkdQ3zuV-OOjG8U","expires_in":28800,"refresh_token":"92f3f897e34f3d8d56db9e812ff39933145a3c582a076196d87b3f0f9b8339fd","scope":"profile activity heartrate","token_type":"Bearer","user_id":"7PMMG7"}"


    fun getHealthData(): HealthData {
        return HealthData(0, 0);
    }

    fun requestAccessToken(code: String) {
        // Formulate the request and handle the response.
        Log.d("Code", code)
        val accessTokenRequest = AccessTokenRequest(
            "$url/$tokenRequestPath",
            code,
            Response.Listener<String> { response ->
                Log.d("FitbitWebAPIClient", response.toString())
                token = response
            },
            Response.ErrorListener { error ->

                Log.e("FitbitWebAPIClient", "ERROR: %s".format(error.message.toString()))
            })

        Log.d("Request", accessTokenRequest.headers.toString())
        Log.d("body", accessTokenRequest.body.toString())

        requestQueue.add(accessTokenRequest)

    }




}