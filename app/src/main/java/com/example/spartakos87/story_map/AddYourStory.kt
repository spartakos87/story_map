package com.example.spartakos87.story_map

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_your_story.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class AddYourStory : AppCompatActivity() {
val    storage = FirebaseStorage.getInstance()

    private val REQUEST_IMAGE = 100
    private val TAG = "MainActivity"
    var destination: File? = null
    var imagePath: String? = null
    var photoUrl : String? = null

    val photoName = dateToString(Date(), "yyyy-MM-dd-hh-mm-ss")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_your_story)
        setSupportActionBar(toolbar)




        val db: FirebaseFirestore


        val intent = intent
        val lat = intent.getStringExtra("lat")
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
           startActivityForResult(intent, REQUEST_IMAGE)



        }

        val txtTitle = findViewById<TextInputEditText>(R.id.textInputEditText2)
        val txtStory = findViewById<EditText>(R.id.editText)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener{

//            Save the data in hashmap Mystory and upload it to firebase database

            val MyStory: HashMap<String, String> = HashMap<String,String>()
            MyStory.put("title",txtTitle.text.toString())
            MyStory.put("story",txtStory.text.toString())
            MyStory.put("lat",lat)
            MyStory.put("lng",lng)
            MyStory.put("url",photoName)
            db.collection("Stories").document().set(MyStory as Map<String, Any>)
//            Return to main activity to map
            val confirm = Intent(this, MapsActivity::class.java)
            startActivity(confirm)

        }





    }


    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            try {

                val `in` = FileInputStream(destination)
                val options = BitmapFactory.Options()
                options.inSampleSize = 10
//                Rotate the image, when we get photo from front camera the image is rotated to firebase
//                So I rotate it before upload it
//                TODO check from which camera take photo, if from front camera rotate the image
//                Here I reduce the quality of image for faster upload and download
                val bmp =rotateImage(90, BitmapFactory.decodeStream(`in`, null, options))
                val baos = ByteArrayOutputStream()
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                val data = baos.toByteArray()
                imagePath = destination!!.getAbsolutePath()
                val storageRef = storage.reference
                val stream = FileInputStream(File(imagePath))
                val picRef = storageRef.child(photoName)
               val  uploadTask = picRef.putBytes(data)
                uploadTask.addOnFailureListener { exception ->


                }.addOnSuccessListener { taskSnapshot ->

//                    TODO Toast which will info user that picture has be uploaded
//                    TODO take download url of image
                }




            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        } else {
                   println("Cancel")
        }
    }



    fun dateToString(date: Date, format: String): String {
//        Create name of photo base to date
//        TODO find really way for unique name
        val df = SimpleDateFormat(format)
        return df.format(date)
    }


    fun rotateImage(angle: Int, bitmapSrc: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.width, bitmapSrc.height, matrix, true)
    }

    }





