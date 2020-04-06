package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import edu.gatech.vera.vera.R


class Startup : AppCompatActivity() {


    var loggedIn: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            onLoginClick()
        }
    }

    /**
     * When the user comes back from the FitbitMobile app this method is run.
     * It will pass the user on to the next activity.
     */
    override fun onResume() {
        super.onResume()
        //checks if they went to the fitbit app
        if (loggedIn) {

            //Redirects to fitbit.com to get auth token
//            intent = Intent(this, AuthLaunchActivity::class.java)
            intent = Intent(this, BadgeLogin::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun onLoginClick() {
        // TODO: change transition animation

        //todo add information alret that you are being redirected to Fitbit app

        //Opens fitbit app if installed
        val packageName = "com.fitbit.FitbitMobile"
        var intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)
        if (intent == null) {
            intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=$packageName")
            }
        }

        startActivity(intent)



        //Not fool-proof method of checking if logged in, just that they went to the fitbit app
        //Change later
        loggedIn = true
    }
}
