package com.example.spartakos87.story_map

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_info_window.*

class InfoWindow : AppCompatActivity() {
    var info_title:String = "No title"
    var info_story:String = "No story"
    var info_photo_url:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_window)
        setSupportActionBar(toolbar)
        val intent = intent
        val DocId:String = intent.getStringExtra("id")
        val db: FirebaseFirestore

        db = FirebaseFirestore.getInstance()
         val doc = db.collection("Stories").document(DocId).get().addOnCompleteListener{task ->


                     if (task.isSuccessful) {
                         val temp  = task.result
                          info_title = temp.get("title").toString()
                          info_story = temp.get("story").toString()
                         info_photo_url = temp.get("url").toString()
                         val txtTitle =  findViewById<TextView>(R.id.textView3)
                         txtTitle.setText(info_title)
                         val txtStory =  findViewById<TextView>(R.id.textView4)
                         txtStory.setText(info_story)


                         val ViewPhoto = findViewById<Button>(R.id.button8)
                         ViewPhoto.setOnClickListener {
//                             Here we parse the firebase url of photo to next activity and we view the image

                         }

                         val DeleteDocument = findViewById<Button>(R.id.button9)
                         ViewPhoto.setOnClickListener {}

                         val EditDocument = findViewById<Button>(R.id.button10)
                         ViewPhoto.setOnClickListener {}

                     }
         }






    }

}
