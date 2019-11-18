package edu.gatech.vera.vera.Controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import edu.gatech.vera.vera.R

class Monitoring : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fitbit = intent.extras.getSerializable("fitbit")
        println("Monitoring $fitbit")
        setContentView(R.layout.activity_monitoring)
    }
}
