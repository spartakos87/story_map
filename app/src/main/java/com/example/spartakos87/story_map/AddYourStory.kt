package com.example.spartakos87.story_map

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_your_story.*









class AddYourStory : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_your_story)
        setSupportActionBar(toolbar)

        val db: FirebaseFirestore





        val intent = intent
        val lat = intent.getStringExtra("lng")
        val lng = intent.getStringExtra("lng")
//        val txt = findViewById<TextInputEditText>(R.id.textInputEditText2)
//        txt.setText("lat===>"+lat)
        db = FirebaseFirestore.getInstance()

//edo pairnei ola ta documents toy collection
//        db.collection("Book")
//                .get()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        for (document in task.result) {
//                            Log.d("INFO",document.id + " => " + document.data)
//                            println("FILIPPAS")
//                        }
//                    } else {
//                        Log.d("INFO", "Error getting documents: ", task.exception)
//                        print("SEREPAS")
//                    }
//                }


        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            val book: HashMap<String, String> = HashMap<String,String>()
            book.put("Title","AYdTO")
            book.put("Author","SEREdPAS")
            book.put("Year","1d987")
            db.collection("Book").document("NewBofkk").set(book as Map<String, Any>)




            // Write a message to the database
//            val database = FirebaseDatabase.getInstance()

//            val myRef = database.getReference("message")


//            // Write a message to the database

//            val myRef = database.getReference("message")
//
//            myRef.setValue("Hello, World!")
            val confirm = Intent(this, MapsActivity::class.java)
            startActivity(confirm)


        }
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }




}
