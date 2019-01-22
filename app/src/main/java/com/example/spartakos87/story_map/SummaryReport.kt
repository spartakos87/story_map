package com.example.spartakos87.story_map

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class SummaryReport : AppCompatActivity() {
    var top3LstUsers:List<String> = emptyList()
    var top3LstPoints:List<Int> = emptyList()
    var usernames = ArrayList<String>()
    var winnerUser:String = ""
    var winnerPoints:Int = 0
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_report)
        val db: FirebaseFirestore
        db = FirebaseFirestore.getInstance()
//        First get distict list of username
        distinctUsernames(db)
//        Calculate  current and preview month tables of points
        val currentPointsTable = readDB(db)
        val previewPointsTable = readDB(db,true)
//        Calculate the top 3 and the winner of preview month
        calculateTop3(currentPointsTable)
        calculateTop3(previewPointsTable,true)
//         Calculate the % for the pie chart
        val winnerPreviewMonth  = findViewById<TextView>(R.id.textView7)
        val top1  = findViewById<TextView>(R.id.textView9)
        val top2  = findViewById<TextView>(R.id.textView10)
        val top3  = findViewById<TextView>(R.id.textView11)

    }


    fun winnerOfPreMonth(){
//        Calculate the winner of previews month

    }


    fun calculateTop3(users:HashMap<String,Int>,preMonth:Boolean=false){
//        Sort the users and return the top3
//        base to this https://www.programiz.com/kotlin-programming/examples/sort-map-values
        val result = users.toList().sortedBy { (_, value) -> value}.toMap()
        if(!preMonth) {
            top3LstUsers = result.keys.toList().take(3)
            top3LstPoints = result.values.toList().take(3)
        }else{
            winnerUser = result.keys.toList().take(1).get(0)
            winnerPoints = result.values.toList().take(1).get(0)

        }



    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    fun readDB(db:FirebaseFirestore,preMonth:Boolean=false): HashMap<String, Int> {
// Read database-firebase and return a hashmap with one key the name of user and the other the
// points
//        val db: FirebaseFirestore
//        db = FirebaseFirestore.getInstance()

//        First we need a distinct list of usernames
//        val usernamesLst = distinctUsernames(db)
        var usersPoints = HashMap<String,Int>()
        for (user in usernames){
            val temp = PersonalReport().readDB(user,preMonth)
            usersPoints.put(user,PersonalReport().calculaterMonthlyPoints(temp, PersonalReport().typeReports))
        }

        return usersPoints
    }

    fun distinctUsernames(db:FirebaseFirestore) {
//        Create a list with all usernames
//        var usernames = ArrayList<String>()
        db.collection("Stories")
                .get().addOnSuccessListener {task ->
                    if (!task.isEmpty){
                        for (document in task.documents) {
                            var username = document.get("username")
                            if (username != null){
                                usernames.add(username as String)
                            }
                        }


                    }}.addOnFailureListener {  }

    usernames =ArrayList(usernames.distinct())
    }


    fun calAllReports(db: FirebaseFirestore){
        var totalFotismos = 0
        var totalAporrimata = 0
        var totalAllo =0
        db.collection("Stories")
                .get().addOnSuccessListener {task ->
                    if (!task.isEmpty){
                        for (document in task.documents) {
                            var type = document.get("type")
                            if (type != null){
                                when (document.get("type")) {
                                    "Φωτισμός" -> totalFotismos += totalFotismos + 1
                                    "Απορρήματα" -> totalAporrimata += totalAporrimata + 1
                                    "Αλλο" -> totalAllo += totalAllo + 1
                                }
                            }
                        }


                    }}.addOnFailureListener {  }


    }

}
