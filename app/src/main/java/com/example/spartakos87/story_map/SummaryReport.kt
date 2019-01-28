package com.example.spartakos87.story_map

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class SummaryReport : AppCompatActivity() {
    var top3LstUsers:List<String> = emptyList()
    var top3LstPoints:List<Int> = emptyList()
    var usernames = ArrayList<String>()
    var winnerUser:String = ""
    var winnerPoints:Int = 0
//    val winnerPreviewMonth  = findViewById<TextView>(R.id.textView7)
//    val top1  = findViewById<TextView>(R.id.textView9)
//    val top2  = findViewById<TextView>(R.id.textView10)
//    val top3  = findViewById<TextView>(R.id.textView11)
    val typeReports = AddYourStory().list_of_choices

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_report)
        val db: FirebaseFirestore
        db = FirebaseFirestore.getInstance()

      readDB2(db)




    }






    @RequiresApi(Build.VERSION_CODES.O)
    fun readDB2(db:FirebaseFirestore,preMonth:Boolean=false){

        var totalFotismos = 0
        var totalAporrimata = 0
        var totalAllo =0
        //                    Make a map with all reports of current user
        val topicMapper: HashMap<String, Int> = HashMap()
//        keys users and value the points of current month
        val currentMonthMapper: HashMap<String, Int> = HashMap()
        //        keys users and value the points of previous month
        val previousMonthMapper: HashMap<String, Int> = HashMap()
        var temp = ""
//        val db: FirebaseFirestore
//        db = FirebaseFirestore.getInstance()
        db.collection("Stories")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        First we loop inside all docs and get a list of unique usernames
                        for (document in task.result) {
                            var username = document.get("username")
                            if (username != null){
                                usernames.add(username as String)
                            }

//                     Inside this for-loop we sum up the kind of reports, to fill the pie chart
                            var type = document.get("type")
                            if (type != null){
                                when (type.toString()) {
                                    "Φωτισμός" -> totalFotismos += totalFotismos + 1
                                    "Απορρήματα" -> totalAporrimata += totalAporrimata + 1
                                    "Αλλο" -> totalAllo += totalAllo + 1
                                }

                            }
                        }
                        usernames =ArrayList(usernames.distinct())
                        topicMapper.put("Φωτισμός" ,totalFotismos)
                        topicMapper.put( "Απορρήματα",totalAporrimata)
                        topicMapper.put("Αλλο",totalAllo)
                        topicMapper.put("total",totalFotismos+totalAporrimata+totalAllo)
//                           TODO push the topicMapper to fun which will create the pie chart
                        calculatePieChartShares(topicMapper)
//                        Next we loop in usernames list and
//                        1st take and calculate the points of current month
//                        2nd calculate the winner of previous month
//                        Calulate the the current and previous month
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = current.format(formatter)
                        val currentMonth = formatted.split("-")[1].toInt()
                        val currentYear = formatted.split("-")[0].toInt()
                        var previousMonth:Int =0
                        var previousYear:Int = 0
                        if (currentMonth !=1){
                             previousMonth = currentMonth -1
                             previousYear = currentYear
                        }else {
                            previousMonth = 12
                             previousYear = currentYear -1
                        }
                        for (user in usernames){
                            var currentFotismos:Int = 0;
                            var currentAporrimata:Int = 0;
                            var currentAllo:Int = 0;
                            var previousFotismos:Int = 0;
                            var previousAporrimata:Int = 0;
                            var previousAllo:Int = 0;
                            for (document in task.result) {

                                var tempDate = document.get("date")
                                if (tempDate != null) {
                                    var saveMonth = tempDate.toString().split("-")[1].toInt()
                                    var saveYear = tempDate.toString().split("-")[0].toInt()


                                var tempUser = document.get("username")
                                if (tempUser != null){
                                    if (tempUser.toString() == user){
                                        var tempType = document.get("type")
                                        if (tempType != null){

                                            if(saveMonth == currentMonth && saveYear==currentYear){
                                            when (tempType.toString()) {
                                                "Φωτισμός" -> currentFotismos += currentFotismos + 1
                                                "Απορρήματα" -> currentAporrimata += currentAporrimata + 1
                                                "Αλλο" -> currentAllo += currentAllo  + 1
                                            }
                                            }

                                            if(saveMonth == previousMonth && saveYear == previousYear){
                                                when (tempType.toString()) {
                                                    "Φωτισμός" -> previousFotismos += previousFotismos + 1
                                                    "Απορρήματα" -> previousAporrimata += previousAporrimata + 1
                                                    "Αλλο" -> previousAllo += previousAllo+ 1
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                            }
//                            Fill the current month hashmap

                            currentMonthMapper.put(user,currentFotismos*10+currentAporrimata*20+currentAllo*30)

                            calculateTop3(currentMonthMapper)
//                            Fill the previous month hashmap
                            previousMonthMapper.put(user,previousFotismos*10+previousAporrimata+previousAllo)

                            calculateTop3(previousMonthMapper,true)


                        }


                    }
                }.addOnFailureListener {

                    topicMapper.put("fail",0)
                }

    }



    fun calculateTop3(users:HashMap<String,Int>,preMonth:Boolean=false){
//        Sort the users and return the top3
//        base to this https://www.programiz.com/kotlin-programming/examples/sort-map-values
        val winnerPreviewMonth  = findViewById<TextView>(R.id.textView7)
        val top1  = findViewById<TextView>(R.id.textView9)
        val top2  = findViewById<TextView>(R.id.textView10)
        val top3  = findViewById<TextView>(R.id.textView11)

        val noDataMsg = "There isn't enough data"
        val result = users.toList().sortedBy { (_, value) -> value}.toMap()
        if(!preMonth) {

//            top3LstUsers = result.keys.toList().take(3)
//            top3LstPoints = result.values.toList().take(3)

            when(result.count()){
                    in 3 .. Int.MAX_VALUE  ->{
                        top3LstUsers = result.keys.toList().take(3)
                        top3LstPoints = result.values.toList().take(3)
                        top1.setText(top3LstUsers[0]+" points:"+top3LstPoints[0])
                        top2.setText(top3LstUsers[1]+" points:"+top3LstPoints[1])
                        top3.setText(top3LstUsers[2]+" points:"+top3LstPoints[2])

                    }
                    2 -> {
                        top3LstUsers = result.keys.toList().take(2)
                        top3LstPoints = result.values.toList().take(2)

                        top1.setText(top3LstUsers[0]+" points:"+top3LstPoints[0])
                        top2.setText(top3LstUsers[1]+" points:"+top3LstPoints[1])
                        top3.setText(noDataMsg)
                    }
                    1 -> {
                        top3LstUsers = result.keys.toList().take(1)
                        top3LstPoints = result.values.toList().take(1)
                        top1.setText(top3LstUsers[0]+" points:"+top3LstPoints[0])
                        top2.setText(noDataMsg)
                        top3.setText(noDataMsg)

                    }
                    else -> {
                        top1.setText(noDataMsg)
                        top2.setText(noDataMsg)
                        top3.setText(noDataMsg)
                    }
                }

            }else{
            if(result.count()>0) {
                winnerUser = result.keys.toList().take(1).get(0)
                winnerPoints = result.values.toList().take(1).get(0)
                winnerPreviewMonth.setText("Is "+winnerUser+" with "+winnerPoints+" points")
            } else{
                    winnerPreviewMonth.setText("There isn't enough data for previous month")
            }
        }



    }

    fun calculatePieChartShares(hashReports: HashMap<String, Int>){
        var percentReports:HashMap<String, Double> = HashMap<String, Double>()
        val total = hashReports["total"]!!.toDouble()
        for (t in typeReports){
            if (total != null && total !=0.0) {
                var temp = hashReports[t]!!.toInt().div(total)

//                percentReports.put(t, temp)
                percentReports.put(t, temp)
            }else{
                percentReports.put(t,0.0)
            }
        }
        // Add pie chart , use this tutorial https://www.codingdemos.com/android-pie-chart-tutorial

        val pieChartView = findViewById<PieChartView>(R.id.chart2)
        val pieValue = ArrayList<SliceValue>()
// TODO Fix the labels of pie chart
        pieValue.add(SliceValue(percentReports[typeReports[0]]!!.toFloat(), Color.BLUE).setLabel(
                typeReports[0]+": "+percentReports[typeReports[0]].toString()
        ))
        pieValue.add(SliceValue(percentReports.get(typeReports[1])!!.toFloat(), Color.BLACK).setLabel(
                typeReports[1]+": "+percentReports[typeReports[1]].toString()
        ))
        pieValue.add(SliceValue(percentReports.get(typeReports[2])!!.toFloat(), Color.RED).setLabel(
                typeReports[2]+": "+percentReports[typeReports[2]].toString()
        ))


        val pieChartData = PieChartData(pieValue)
        pieChartView.setPieChartData(pieChartData);

    }


}
