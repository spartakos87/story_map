package com.example.spartakos87.story_map

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.TextView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import lecho.lib.hellocharts.view.PieChartView
import lecho.lib.hellocharts.model.SliceValue
import java.util.ArrayList
import lecho.lib.hellocharts.model.PieChartData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PersonalReport : AppCompatActivity() {
    var username: String = ""
    val typeReports = AddYourStory().list_of_choices
    val totalTypeReports = arrayOf("totalΦωτισμός","totalΑπορρήματα","totalΑλλο")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_report)

        val intent = intent
         username = intent.getStringExtra("username")
        val data = readDB(username)
        println("SEREPAS"+data)
//        Check if data contain the key fail, if it is pop up a msg
        val points = calculaterMonthlyPoints(data,typeReports) // Currnet month points
        val totalPoints = calculaterMonthlyPoints(data,totalTypeReports) // Total points
        val chartPercentans = calculatePieChartShares(data, typeReports)
        val txtUsername = findViewById<TextView>(R.id.textView)
        val txtPointsMonth = findViewById<TextView>(R.id.textView2)
        val txtPointsSum = findViewById<TextView>(R.id.textView5)
        txtUsername.setText("Hello "+username)
        txtPointsMonth.setText("Points the current month: "+points)
        txtPointsSum.setText("Points summary; "+totalPoints)


// Add pie chart , use this tutorial https://www.codingdemos.com/android-pie-chart-tutorial

        val pieChartView = findViewById<PieChartView>(R.id.chart)
val pieValue = ArrayList<SliceValue>()
        pieValue.add(SliceValue(chartPercentans[typeReports[0]]!!.toFloat(), Color.BLUE).setLabel(
                typeReports[0]+": "+chartPercentans[typeReports[0]].toString()
        ))
        pieValue.add(SliceValue(chartPercentans.get(typeReports[1])!!.toFloat(), Color.BLACK).setLabel(
                typeReports[1]+": "+chartPercentans[typeReports[1]].toString()
        ))
        pieValue.add(SliceValue(chartPercentans.get(typeReports[2])!!.toFloat(), Color.RED).setLabel(
                typeReports[1]+": "+chartPercentans[typeReports[2]].toString()
        ))


        val pieChartData = PieChartData(pieValue)
        pieChartView.setPieChartData(pieChartData);


    }

    fun calculaterMonthlyPoints(hashReports:HashMap<String,Int>,typeReports:Array<String>):Int{
//        Retrive from firebase all reports from the current user
//        and calculate the current month points of him
//        val typeReports = AddYourStory().list_of_choices
        var points:Int = 0
        for (t in typeReports){

            when(t){
                typeReports[0] -> points += hashReports[t]!!*10
                typeReports[1] -> points += hashReports[t]!!*20
                typeReports[2] -> points += hashReports[t]!!*30
            }
        }
        return points
    }


    fun calculatePieChartShares(hashReports: HashMap<String, Int>,typeReports:Array<String>):HashMap<String, Double>{
        var percentReports:HashMap<String, Double> = HashMap<String, Double>()
        val total = hashReports.get("total")!!.toDouble()
//        val typeReports = AddYourStory().list_of_choices
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
    fun readDB(username: String,preMonth:Boolean=false):HashMap<String,Int>{
        println("In DB SEREPAS")
        //                    Make a map with all reports of current user
        val topicMapper: HashMap<String, Int> = HashMap<String,Int>()
        var fotismos:Int = 0;
        var aporrimata:Int = 0;
        var allo:Int = 0;
        var totalFotismos:Int = 0;
        var totalAporrimata:Int = 0;
        var totalAllo:Int = 0;
        var tTotal: Int = 0

        var total:Int = 0

        val db: FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("Stories")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {


                        println("SEREPAS TASK NOT EMPTY")
                        for (document in task.result) {
                            val user = document.get("username")
                            if (user != null) {
                                //First we check if there is the field of username, for legacy reasons mostly
                                if (document.get("username").toString() == username) {
                                    // the next step is to check the type
//                                    in this step calculate the total report for the current user

                                    val type = document.get("type")
                                    if (type != null) {
                                        when (document.get("type")) {
                                            "Φωτισμός" -> totalFotismos += totalFotismos + 1
                                            "Απορρήματα" -> totalAporrimata += totalAporrimata + 1
                                            "Αλλο" -> totalAllo += totalAllo + 1
                                        }
                                    }
                                } else {
//                                    TODO Ignore this data

//                                    Then if this report belong to current user we check the date
                                    val current = LocalDateTime.now()
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                    val formatted = current.format(formatter)
                                    val currentMonth = formatted.split("-")[1].toInt()
                                    val currentYear = formatted.split("-")[0].toInt()
                                    val date = document.get("date")
                                    if (date != null) {
                                        var saveMonth = date.toString().split("-")[1].toInt()
                                        var saveYear = date.toString().split("-")[0].toInt()
// Check the var preMonth. If is false we continue with the current month and year else we calculate
// for previews month and year
                                        if (preMonth){
                                            if (saveMonth-1 == 0){
                                                saveMonth =12
                                                saveYear -= 1
                                            }else{
                                                saveMonth -= 1
                                            }
                                        }
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
                                            println("SEREPAS empty")
                                }
                            }

                        }}
                        }
                        total = fotismos+aporrimata+allo
                        println("SEREPAS TOTAL:"+total)
                        tTotal = totalFotismos+totalAporrimata+totalAllo
                        println("SEREPAS tTOTAL:"+tTotal)
                        }else{
                        println("SEREPAS empty2")

                    }



                  topicMapper.put("Φωτισμός" ,fotismos)
                    topicMapper.put( "Απορρήματα",aporrimata)
                    topicMapper.put("Αλλο",allo)
                  topicMapper.put("totalΦωτισμός" ,totalFotismos)
                    topicMapper.put( "totalΑπορρήματα",totalAporrimata)
                    topicMapper.put("totalΑλλο",totalAllo)
                    topicMapper.put("total",total)
                    topicMapper.put("tTotal",tTotal)


                }.addOnFailureListener {
                    println("SEREPAS fail ")
topicMapper.put("fail",0)
                }
        return topicMapper
    }



}
