package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.util.Log
import edu.gatech.vera.vera.R
import android.view.View
import android.widget.Button
import android.widget.Toast

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
            onLoginClick(it)
        }
    }

    private fun onLoginClick(view: View) {
        if (verifyFields()) {
            val intent = Intent(this, Monitoring::class.java)
            intent.putExtra("fitbit", "Fitbit 01")
            startActivity(intent)
        } else {
            val toast = Toast.makeText(this@BadgeLogin, "Incorrect Badge Number/Password", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun verifyFields(): Boolean {
        val badge = findViewById<TextInputLayout>(R.id.badge)
        val password = findViewById<TextInputLayout>(R.id.password)

        val badgeTxt = badge.editText?.text.toString()
        val passwordTxt = password.editText?.text.toString()

        return badgeTxt.equals("00911") && passwordTxt.equals("password")
    }
}
