package edu.gatech.vera.vera.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Button
import edu.gatech.vera.vera.R

class SelectFitbit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_fitbit)

        setUpLogout()

        val fitbitSelector : RecyclerView = findViewById(R.id.fitbitSelector)
        loadSelector(fitbitSelector)
    }

    private fun getFitbitList() : ArrayList<String> {
        val fitbitList = arrayListOf<String>()

        for (i in 1..5) {
            fitbitList.add("Fitbit 10$i")
        }

        return fitbitList
    }

    private fun loadSelector(fitbitSelector : RecyclerView) {
        fitbitSelector.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val fitbitList = getFitbitList()
        fitbitSelector.adapter = SelectFitbitAdapter(fitbitList, {fitbit : String -> connectFitbit(fitbit)})

        val divider = DividerItemDecoration(fitbitSelector.context, LinearLayout.VERTICAL)
        divider.setDrawable(fitbitSelector.context.resources.getDrawable(R.drawable.select_fitbit_list_divider))
        fitbitSelector.addItemDecoration(divider)
    }

    private fun connectFitbit(fitbit : String) {
        println("connectFitbit() -> $fitbit")
        val intent = Intent(this, Monitoring::class.java)
        intent.putExtra("fitbit", fitbit)
        startActivity(intent)
    }

    private fun setUpLogout() {
        val logoutButton = findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            val intent = Intent(this, Startup::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        println("log out user")
    }
}
