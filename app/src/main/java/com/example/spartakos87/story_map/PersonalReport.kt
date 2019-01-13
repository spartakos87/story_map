package com.example.spartakos87.story_map

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.TextView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.firestore.FirebaseFirestore
import lecho.lib.hellocharts.view.PieChartView
import lecho.lib.hellocharts.model.SliceValue
import java.util.ArrayList
import lecho.lib.hellocharts.model.PieChartData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PersonalReport : AppCompatActivity() {
    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_report)

        val intent = intent
         username = intent.getStringExtra("username")
        println("SEREPAS current user "+ username)
        readDB(username)
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

    fun calculaterMonthlyPoint(hashReports:HashMap<String,Int>):Int{
//        Retrive from firebase all reports from the current user
//        and calculate the current month points of him
        val typeReports = AddYourStory().list_of_choices
        var points:Int = 0
        for (t in typeReports){
            when(t){
                typeReports[0] -> points += hashReports.get(t)!!*10
                typeReports[1] -> points += hashReports.get(t)!!*20
                typeReports[2] -> points += hashReports.get(t)!!*30
            }
        }
        return points
    }


    fun calculatePieChartShares(hashReports: HashMap<String, Int>):HashMap<String, Double>{
        var percentReports:HashMap<String, Double> = HashMap<String, Double>()
        val total = hashReports.get("total")!!.toDouble()
        val typeReports = AddYourStory().list_of_choices
        for (t in typeReports){
            if (total != null && !total.equals(0)) {
                var temp = hashReports.get(t)!!.toInt().div(total)

                percentReports.put(t, temp)
            }else{
                percentReports.put(t,0.0)
            }
        }
return percentReports
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun readDB(username: String):HashMap<String,Int>{
        //                    Make a map with all reports of current user
        val topicMapper: HashMap<String, Int> = HashMap<String,Int>()
        var fotismos:Int = 0;
        var aporrimata:Int = 0;
        var allo:Int = 0;
        var total:Int = 0

        val db: FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("Stories")
                .get().addOnSuccessListener {task ->
                    if (!task.isEmpty){
                        for (document in task.documents) {
                            val user = document.get("username")
                            if (user != null) {
                                //First we check if there is the field of username, for legacy reasons mostly
                                if (document.get("username").toString() == username) {
//                                    Then if this report belong to current user we check the date
                                    val current = LocalDateTime.now()
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                    val formatted = current.format(formatter)
                                    val currentMonth = formatted.split("-")[1].toInt()
                                    val currentYear = formatted.split("-")[0].toInt()
                                    val date = document.get("date")
                                    if (date != null) {
                                        val saveMonth = date.toString().split("-")[1].toInt()
                                        val saveYear = date.toString().split("-")[0].toInt()

                                        if (currentMonth == saveMonth && currentYear == saveYear) {
//                                            The report is from current month

//                                        }
//                                    }


//                                    and the next step is to check the type
                                    val type = document.get("type")
                                    if (type != null) {
                                        when (document.get("type")) {
                                            "Φωτισμός" -> fotismos += fotismos + 1
                                            "Απορρήματα" -> aporrimata += aporrimata + 1
                                            "Αλλο" -> allo += allo + 1
                                        }
                                    }
                                } else {
//                                    TODO Ignore this data
                                }
                            }

                        }}
                        }
                        total = fotismos+aporrimata+allo
                        }



                  topicMapper.put("Φωτισμός" ,fotismos)
                    topicMapper.put( "Απορρήματα",aporrimata)
                    topicMapper.put("Αλλο",allo)
                    topicMapper.put("total",total)



                }.addOnFailureListener {
topicMapper.put("fail",0)
                }
       return topicMapper
    }
}
