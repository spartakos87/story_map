package com.example.spartakos87.story_map

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_view.*

class ImageView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        setSupportActionBar(toolbar)
        val intent = intent
        val photoUrl:String = intent.getStringExtra("photourl")
Glide
        .with(this)
        .load(photoUrl)
        .into(findViewById(R.id.imageView))






    }

}
