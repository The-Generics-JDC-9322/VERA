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
        val intent = Intent(this, AuthLaunchActivity::class.java)
        startActivity(intent)
    }
}
