package com.example.spartakos87.story_map

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_your_story.*
import java.io.File






class AddYourStory : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_your_story)
        setSupportActionBar(toolbar)

        val db: FirebaseFirestore

var takeUri:Uri


        val intent = intent
        var path = ""
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
             path = fileUri.toString()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager) != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)


            }
        }



@Override
fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

    if (resultCode == Activity.RESULT_OK
            && requestCode == REQUEST_IMAGE_CAPTURE) {

        val extras = data.extras
        val imageBitmap = extras.get("data") as Bitmap


    } else {
        super.onActivityResult(requestCode, resultCode, data)
    }

}







        val takephoto = findViewById<Button>(R.id.button2)
        takephoto.setOnClickListener {
//        dispatchTakePictureIntent()
launchCamera()
//            val myFile = File(path.toString())

//            myFile.getAbsolutePath()

println("=====================================>"+path)
            val myFile = File(path)
println("=====================================>"+myFile.exists())
            println("AUTO==============>"+myFile.absolutePath)




//
//            val storage = FirebaseStorage.getInstance()
//            val storageRef = storage.reference
//
//            var file = Uri.fromFile(File(path))
//            val riversRef = storageRef.child("images/${file.lastPathSegment}")
//            riversRef.putFile(file)

        }



        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            val book: HashMap<String, String> = HashMap<String,String>()
            book.put("lat",lat)
            book.put("lng",lng)

//            book.put("Img", thePhoto.toString())
//            book.put("Year","1d987")
            db.collection("Book").document().set(book as Map<String, Any>)




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
