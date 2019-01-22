package com.example.spartakos87.story_map

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.jetbrains.anko.startActivity

class Welcom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom)
        val logitBtn = findViewById<Button>(R.id.button3)
        val registerBtn = findViewById<Button>(R.id.button4)

        logitBtn.setOnClickListener {
            val login = Intent(this, Login::class.java)
            startActivity(login)
        }

        registerBtn.setOnClickListener {
            val register = Intent(this, Register::class.java)
            startActivity(register)
        }
    }
}
