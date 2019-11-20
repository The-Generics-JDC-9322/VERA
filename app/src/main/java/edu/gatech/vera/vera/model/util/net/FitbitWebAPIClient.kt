package edu.gatech.vera.vera.model.util.net

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import edu.gatech.vera.vera.model.HealthData
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

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
        val accessToken = FitbitAPI.access_token
        val url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json"
        //val url = "https://api.fitbit.com/1/user/-/profile.json"
        var heartrateData : JSONObject = JSONObject(HashMap<String, String>())

        var healthData: HealthData = HealthData(0,0)
        val request = object : JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                // Process the json
                var maxHeartrate = response.toString().substring(response.toString().indexOf("max") + 5, response.toString().indexOf("max") + 7)

                Log.d("LOG", response.toString())
//                bpm.setText("$maxHeartrate bpm")

                healthData = HealthData(maxHeartrate.toInt(), 0)


            }, Response.ErrorListener{ error ->
                // Error in request

                val data = String(error.networkResponse.data, StandardCharsets.UTF_8)
                Log.d("LOG", data)
            })
        {
            override fun getHeaders(): HashMap<String, String> {
                val headers = HashMap<String, String>()
//                val data = "22BFC6:a4469ef766024f63bb91726ddcea0e4f"
//                val auth = Base64.encodeToString(data.toByteArray(), Base64.DEFAULT).trim()
//
//                Log.d("LOG", "$auth this is a test")
//                Log.d("LOG", auth)
                headers.put("Authorization", "Bearer $accessToken")
                headers.put("Content-Type", "application/x-www-form-urlencoded")

                return headers

            }
        }
        requestQueue.add(request)

        return healthData
    }

    fun requestAccessToken(code: String) {
        // Formulate the request and handle the response.
        Log.d("Code", code)
        val accessTokenRequest = AccessTokenRequest(
            "$url/$tokenRequestPath",
            code,
            Response.Listener<String> { response ->
                Log.d("FitbitWebAPIClient", response.toString())
                Log.d("AccessToken", JSONObject(response).get("access_token").toString())
                token = response
                FitbitAPI.access_token = JSONObject(response).get("access_token").toString()
            },
            Response.ErrorListener { error ->

                Log.e("FitbitWebAPIClient", "ERROR: %s".format(error.message.toString()))
            })

        Log.d("Request", accessTokenRequest.headers.toString())
        Log.d("body", accessTokenRequest.body.toString())

        requestQueue.add(accessTokenRequest)

    }




}