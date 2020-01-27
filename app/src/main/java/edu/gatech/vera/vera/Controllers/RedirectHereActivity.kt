package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import edu.gatech.vera.vera.R
import edu.gatech.vera.vera.model.util.net.FitbitWebAPIClient



class RedirectHereActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect_here)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {

            appLinkData?.also {  uri ->
                val code = uri.getQueryParameter("code")
                FitbitWebAPIClient.requestAccessToken(code)
            }

        }

        val intent = Intent(this, SelectFitbit::class.java)
        startActivity(intent)
    }

}
