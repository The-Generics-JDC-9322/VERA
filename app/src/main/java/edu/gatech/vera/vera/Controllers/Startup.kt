package edu.gatech.vera.vera.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import edu.gatech.vera.vera.R

import android.content.Intent
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.Volley.*
import edu.gatech.vera.vera.model.util.net.FitbitAPI
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8

class Startup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            onLoginClick(it)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun onLoginClick(view: View) {
        // TODO: change transition animation
        //val intent = Intent(this, AuthLaunchActivity::class.java)
//        Log.d("LOG","something")
//        val url = "https://api.fitbit.com/oauth2/token"
//        val params = HashMap<String,String>()
//
//        params["client_id"] = "22BFC7"
//        params["grant_type"] = "authorization_code"
//        params["redirect_uri"] = "https://bjvsavutig.execute-api.us-east-2.amazonaws.com/default/test?code=ff4e82cc52fde74119f78b587a8e62a8954c5609#_="
//
//
//        val data : ByteArray = "client_id=22BFC7&grant_type=authorization_code&code=ad8bda8d8a3611b3b177a74f19105668026d9ebe&redirect_uri=https%3A%2F%2Fnavaem8.wixsite.com".toByteArray(StandardCharsets.UTF_8)
//
//        val jsonObject = JSONObject(params)
//        val queue = Volley.newRequestQueue(this)
//        val request = object : JsonObjectRequest(Request.Method.POST,url,jsonObject,
//            Response.Listener { response ->
//                // Process the json
//                Log.d("LOG", response.toString())
//
//
//            }, Response.ErrorListener{ error ->
//                // Error in request
//
//            val data = String(error.networkResponse.data, UTF_8)
//                Log.d("LOG", data)
//            })
//        {
//
//            override fun getHeaders(): HashMap<String, String> {
//                val headers = HashMap<String, String>()
//                val data = "22BFC7:e81d8c0447eb0592f5e7e9d0b7e6905d"
//                val auth = Base64.encodeToString(data.toByteArray(), Base64.DEFAULT).trim()
//
//                Log.d("LOG", "$auth this is a test")
//                Log.d("LOG", auth)
//                headers.put("Authorization", "Basic $auth")
//                headers.put("Content-Type", "application/x-www-form-urlencoded")
//
//                return headers
//
//            }
//
//            override fun getBody() : ByteArray {
//                return data
//            }
//        }
//        queue.add(request)
//        val accessToken = FitbitAPI.access_token
//        val url = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json"
//        //val url = "https://api.fitbit.com/1/user/-/profile.json"
//        val queue = Volley.newRequestQueue(this)
//        var heartrateData : JSONObject = JSONObject(HashMap<String, String>())
//        val request = object : JsonObjectRequest(Request.Method.GET,url,null,
//            Response.Listener { response ->
//                // Process the json
//                heartrateData = response
//                Log.d("LOG", response.toString())
//
//
//
//            }, Response.ErrorListener{ error ->
//                // Error in request
//
//                val data = String(error.networkResponse.data, UTF_8)
//                Log.d("LOG", data)
//            })
//        {
//            override fun getHeaders(): HashMap<String, String> {
//                val headers = HashMap<String, String>()
////                val data = "22BFC6:a4469ef766024f63bb91726ddcea0e4f"
////                val auth = Base64.encodeToString(data.toByteArray(), Base64.DEFAULT).trim()
////
////                Log.d("LOG", "$auth this is a test")
////                Log.d("LOG", auth)
//                headers.put("Authorization", "Bearer $accessToken")
//                headers.put("Content-Type", "application/x-www-form-urlencoded")
//
//                return headers
//
//            }
//        }
//        queue.add(request)

        val intent = Intent(this, SelectFitbit::class.java)
        startActivity(intent)
    }
}
