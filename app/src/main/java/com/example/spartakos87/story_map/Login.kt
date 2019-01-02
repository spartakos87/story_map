package com.example.spartakos87.story_map

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();
        val ETemail= findViewById<EditText>(R.id.editText2)
        val ETpassword = findViewById<EditText>(R.id.editText3)



        val btn = findViewById<Button>(R.id.button5)
        btn.setOnClickListener {
            val email:String = ETemail.text.toString()
            val password:String = ETpassword.text.toString()
            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val text = "Success login"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()

//                move to map activity
                // if the created of new account is success go to map activity with the mail of user
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("username", email)
                startActivity(intent)
            }.addOnFailureListener {

                val text = "Failed to login"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
    }
}
