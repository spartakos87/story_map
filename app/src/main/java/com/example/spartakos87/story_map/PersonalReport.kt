package com.example.spartakos87.story_map

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class PersonalReport : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_report)

        val intent = intent
        val username = intent.getStringExtra("username")
        val txtUsername = findViewById<TextView>(R.id.textView)
        val txtPointsMonth = findViewById<TextView>(R.id.textView2)
        val txtPointsSum = findViewById<TextView>(R.id.textView5)
        txtUsername.setText("Hello "+username)
        txtPointsMonth.setText("Points the current month")
        txtPointsSum.setText("Points summary")

        
    }
}
