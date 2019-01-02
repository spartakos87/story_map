package com.example.spartakos87.story_map

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth



class Register : AppCompatActivity() {
     lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance();
        val ETemail = findViewById<EditText>(R.id.editText4)
        val ETpassword = findViewById<EditText>(R.id.editText5)
        val btn = findViewById<Button>(R.id.button6)
        btn.setOnClickListener {
            val email = ETemail.text.toString()
            val password = ETpassword.text.toString()

            if (mAuth != null) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        //Registration OK
                        val text = "Registration was success"
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()

// if the created of new account is success go to map activity with the mail of user
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.putExtra("username", email)
                        startActivity(intent)

                    } else {
                        //Registration error
                        val text = "Registration was failed"
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }
                }
            }else {
                val text = "Something went wrong"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
    }
}
