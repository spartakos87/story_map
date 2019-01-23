package com.example.spartakos87.story_map

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Reports : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        //        Here we have two buttons
//        one leads to personal report
//        the other to summary report
        val intent = intent
        val username = intent.getStringExtra("username")
        val btnPersonalReports = findViewById<Button>(R.id.button7)
        val btnSummaryReport = findViewById<Button>(R.id.button11)

        btnPersonalReports.setOnClickListener {

//            Here we need the username of the current user to recreate the report
            val intent = Intent(this, PersonalReport::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }


        btnSummaryReport.setOnClickListener {
//            In this case we dont need the username of current user
            val intent = Intent(this, SummaryReport::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}
