package com.example.spartakos87.story_map

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import lecho.lib.hellocharts.view.PieChartView
import lecho.lib.hellocharts.model.SliceValue
import java.util.ArrayList
import lecho.lib.hellocharts.model.PieChartData




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

// Add pie chart , use this tutorial https://www.codingdemos.com/android-pie-chart-tutorial
        val pieChartView = findViewById<PieChartView>(R.id.chart)
val pieValue = ArrayList<SliceValue>()
        pieValue.add(SliceValue(20f, Color.BLUE))
        pieValue.add(SliceValue(70f, Color.BLACK))
        pieValue.add(SliceValue(10f, Color.RED))


        val pieChartData = PieChartData(pieValue)
        pieChartView.setPieChartData(pieChartData);


    }
}
