package com.example.spartakos87.story_map

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_image_view.*

class ImageView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        setSupportActionBar(toolbar)
        val intent = intent
        val photoName:String = intent.getStringExtra("photourl")
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child(photoName).downloadUrl.addOnSuccessListener {url ->
            // Got the download URL for 'users/me/profile.png'
            Glide
                    .with(this)
                    .load(url)
                    .into(findViewById(R.id.imageView))



        }.addOnFailureListener {
            // Handle any errors
        }







    }

}
