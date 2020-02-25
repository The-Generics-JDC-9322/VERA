package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import android.widget.Toast
import edu.gatech.vera.vera.R

class BadgeLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badge_login)
        setupInputs()
    }

    private fun setupInputs() {
        val badgeInput = findViewById<TextInputLayout>(R.id.badge)
        badgeInput.editText?.inputType = InputType.TYPE_CLASS_NUMBER

        val loginButton = findViewById<Button>(R.id.loginButton2)
        loginButton.setOnClickListener {
            onLoginClick()
        }
    }

    private fun onLoginClick() {
        val badgeNumber = findViewById<TextInputLayout>(R.id.badge).editText?.text.toString()
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text.toString()

        if (verifyFields(badgeNumber, password)) {
            val intent = Intent(this, Monitoring::class.java)
            intent.putExtra("fitbit", "Fitbit 01")
            intent.putExtra("badgeNumber", badgeNumber)
            startActivity(intent)
        } else {
            val toast = Toast.makeText(this@BadgeLogin, "Incorrect Badge Number/Password", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun verifyFields(badgeNumber: String, password: String): Boolean {
        // TODO: implement Badge Login Service
        return badgeNumber.equals("00911") && password.equals("password")
    }

    override fun onBackPressed() {
        // TODO: implement logout
        val intent = Intent(this, Startup::class.java)
        startActivity(intent)
    }
}
