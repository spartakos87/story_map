package com.example.spartakos87.story_map

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_your_story.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*




@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddYourStory : AppCompatActivity() {
val    storage = FirebaseStorage.getInstance()

    private val REQUEST_IMAGE = 100
    private val TAG = "MainActivity"
    var destination: File? = null
    var imagePath: String? = null
    var photoUrl : Uri? = null

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
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build());

        val name = dateToString(Date(), "yyyy-MM-dd-hh-mm-ss")
        destination = File(Environment.getExternalStorageDirectory(), "$name.jpg")

        val takephoto = findViewById<Button>(R.id.button2)
        takephoto.setOnClickListener {


            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
            startActivityForResult(intent, REQUEST_IMAGE);



        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                val `in` = FileInputStream(destination)
                val options = BitmapFactory.Options()
                options.inSampleSize = 10
                imagePath = destination!!.getAbsolutePath()
                println("~~~~~~~~~~~~~~~~~~~~~~~>"+imagePath)
                val storageRef = storage.reference
                val stream = FileInputStream(File(imagePath))
                val picRef = storageRef.child(dateToString(Date(), "yyyy-MM-dd-hh-mm-ss"))

               val  uploadTask = picRef.putStream(stream)
                uploadTask.addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                    println("@@@@@@@@@@@@@@@@@@@>NOPE")
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                    println("&&&&&&&&&&&&&&&&&&&> OK")
                    picRef.downloadUrl.addOnCompleteListener () {taskSnapshot ->

//                        var url = taskSnapshot.result
                        photoUrl = taskSnapshot.result
//                        println ("url =" + url.toString ())

                    }
                }
//                val file = File(imagePath)
//                println("++++++++++++++++++++++++++++++>"+file.exists().toString())

                val bmp = BitmapFactory.decodeStream(`in`, null, options)

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        } else {
println("Cancel")
        }
    }



    fun dateToString(date: Date, format: String): String {
        val df = SimpleDateFormat(format)
        return df.format(date)
    }

    }





