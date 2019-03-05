package com.example.spartakos87.story_map

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import org.jetbrains.anko.startActivity

class Welcom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom)
        val logitBtn = findViewById<Button>(R.id.button3)
        val registerBtn = findViewById<Button>(R.id.button4)
        val anonymousBtn = findViewById<Button>(R.id.anonymous)

        logitBtn.setOnClickListener {
            val login = Intent(this, Login::class.java)
            startActivity(login)
        }

        registerBtn.setOnClickListener {
            val register = Intent(this, Register::class.java)
            startActivity(register)
        }


        anonymousBtn.setOnClickListener {
            /*
            Add button for anonymous login. The use will be login in map activity as
            the user 'anonymous'.
             */
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("username", "anonymous")
            startActivity(intent)
        }
    }
}
