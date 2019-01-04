package com.example.spartakos87.story_map

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_info_window.*

class InfoWindow : AppCompatActivity() {
    var info_title:String = "No title"
    var info_story:String = "No story"
    var info_photo_url:String = ""
    var reporter:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_window)
        setSupportActionBar(toolbar)
        val intent = intent
        val DocId:String = intent.getStringExtra("id")
        val username:String = intent.getStringExtra("username")
        val db: FirebaseFirestore

        db = FirebaseFirestore.getInstance()
         val doc = db.collection("Stories").document(DocId).get().addOnCompleteListener{task ->


                     if (task.isSuccessful) {
                         val temp = task.result
                         info_title = temp.get("title").toString()
                         info_story = temp.get("story").toString()
                         info_photo_url = temp.get("url").toString()
                         if (temp.get("username") != null) {
                             reporter = temp.get("username").toString()
                         }
                         val txtTitle = findViewById<TextView>(R.id.textView3)
                         txtTitle.setText(info_title)
                         val txtStory = findViewById<TextView>(R.id.textView4)
                         txtStory.setText(info_story)


                         val ViewPhoto = findViewById<Button>(R.id.button8)
                         ViewPhoto.setOnClickListener {
                             //                             Here we parse the firebase url of photo to next activity and we view the image
                             val imageViewActiviyIntent = Intent(this, ImageView::class.java)
                             imageViewActiviyIntent.putExtra("photourl", info_photo_url)
                             startActivity(imageViewActiviyIntent)


                         }

                         val DeleteDocument = findViewById<Button>(R.id.button10)
                         if (username.equals(reporter)){
//                             if username and reporter is the same person then user can delete his report
                             DeleteDocument.setOnClickListener {

                                 db.collection("Stories").document(DocId).delete().addOnSuccessListener {
                                     //If the delete action was success return to main activity, to map
//                             Here we attempt to delete the image which is connected with the document
                                     val storage = FirebaseStorage.getInstance()

                                     val storageRef = storage.reference
                                     // Create a storage reference from our app

                                     // Create a reference to the file to delete
                                     val desertRef = storageRef.child(info_photo_url)
                                     desertRef.delete().addOnSuccessListener {

                                         // if the document  and image are deleted go to main activity
                                         val confirm = Intent(this, MapsActivity::class.java)
                                         startActivity(confirm)
                                     }.addOnFailureListener {
                                         //                                   TODO msg that fail to delete the image
                                     }
                                 }.addOnFailureListener {
                                     //TODO make Toast which said that delete proceduere had failed
                                 }
                             }

                     }else{
//                             Otherwise you cant and appear a toast
                             val text = "You can't delete this report"
                             val duration = Toast.LENGTH_SHORT

                             val toast = Toast.makeText(applicationContext, text, duration)
                             toast.show()
                         }


                         val EditDocument = findViewById<Button>(R.id.button9)
                         EditDocument.setOnClickListener {
//                             TODO all the functionality of edit we will be placed here
                         }

                     }
         }






    }

}
