package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import edu.gatech.vera.vera.R

/**
 * This is the Activity where an officer can put in their badge number.
 * It will be recorded and passed to the monitoring Activity. It is usually
 * started from the Startup Activity.
 *
 * @see Monitoring
 * @see Startup
 */
class BadgeLogin : AppCompatActivity() {

    /**
     * Creates the BadgeLogin Activity. This method will call setupInputs()
     * which will add an OnClickListener to the loginButton.
     *
     * @param savedInstanceState the saved instance State.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badge_login)
        setupInputs()
    }

    /**
     * This method adds an onclick listener to the login button and makes
     * the badgeInput only accept numbers.
     */
    private fun setupInputs() {
        val badgeInput = findViewById<TextInputLayout>(R.id.badge)
        badgeInput.editText?.inputType = InputType.TYPE_CLASS_NUMBER

        val loginButton = findViewById<Button>(R.id.loginButton2)
        loginButton.setOnClickListener {
            onLoginClick()
        }
    }

    /**
     * This is the onclick handler for the login button. It will store the
     * inputted badge number in the intent and pass that intent to the
     * Monitoring Activity class.
     */
    private fun onLoginClick() {
        val badgeNumber = findViewById<TextInputLayout>(R.id.badge).editText?.text.toString()

        val intent = Intent(this, Monitoring::class.java)
        intent.putExtra("fitbit", "Fitbit 01")
        intent.putExtra("badgeNumber", badgeNumber)
        startActivity(intent)

    }

    /**
     * This is a method to handle pressing the Android back button. It will
     * take the user back to the Startup Activity screen.
     */
    override fun onBackPressed() {
        // TODO: implement logout
        val intent = Intent(this, Startup::class.java)
        startActivity(intent)
    }
}
