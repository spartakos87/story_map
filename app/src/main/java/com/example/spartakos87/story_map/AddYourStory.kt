package com.example.spartakos87.story_map

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_your_story.*






@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddYourStory : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_your_story)
        setSupportActionBar(toolbar)

        val db: FirebaseFirestore


        val intent = intent
        var path = ""
        val lat = intent.getStringExtra("lng")
        val lng = intent.getStringExtra("lng")
        db = FirebaseFirestore.getInstance()


        val takephoto = findViewById<Button>(R.id.button2)
        takephoto.setOnClickListener {

            launchCamera()





        }

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            val book: HashMap<String, String> = HashMap<String,String>()
            book.put("lat",lat)
            book.put("lng",lng)

            db.collection("Book").document().set(book as Map<String, Any>)


            val confirm = Intent(this, MapsActivity::class.java)
            startActivity(confirm)


        }

    }

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
println("test=======================================>")
        if (resultCode == Activity.RESULT_OK
                && requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getData() != null) {

          println("OK==============================>")
            val imageBitmap = data.extras.get("data") as Bitmap
println("~~~~~~~~~~~~~~~~~~~>"+data.data.path.toString())
        } else {


            println("``````````````1111111111111111111111111111111 nothing")
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


    val REQUEST_IMAGE_CAPTURE = 1


         fun dispatchTakePictureIntent() {

             Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

                takePictureIntent.resolveActivity(packageManager)?.also {


                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }




         }



        fun launchCamera() {
            val values = ContentValues(1)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            val fileUri = contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values)

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager) != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)


            }
        }




    }





