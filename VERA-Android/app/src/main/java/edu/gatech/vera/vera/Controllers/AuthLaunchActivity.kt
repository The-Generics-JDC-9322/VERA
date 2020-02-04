package edu.gatech.vera.vera.Controllers

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.customtabs.CustomTabsIntent
import edu.gatech.vera.vera.model.util.net.FitbitAPI

@Deprecated("Startup goes straight to Badge Login")
class AuthLaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth_Url: String = "https://www.fitbit.com/oauth2/authorize"
        val url_params: String = "?response_type=code" +
                "&client_id=" + FitbitAPI.clientID +
                "&redirect_uri=" + FitbitAPI.callbackURI +
                "&scope=profile%20heartrate%20activity"

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(auth_Url + url_params))

    }
}