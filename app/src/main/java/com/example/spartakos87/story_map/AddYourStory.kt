package com.example.spartakos87.story_map

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*






//@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddYourStory : AppCompatActivity() {
val    storage = FirebaseStorage.getInstance()

    private val REQUEST_IMAGE = 100
    private val TAG = "MainActivity"
    var destination: File? = null
    var imagePath: String? = null
    var photoUrl : String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_your_story)
        setSupportActionBar(toolbar)


        val db: FirebaseFirestore


        val intent = intent
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
           startActivityForResult(intent, REQUEST_IMAGE)



        }

        val txtTitle = findViewById<TextInputEditText>(R.id.textInputEditText2)
        val txtStory = findViewById<EditText>(R.id.editText)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener{

            println("????????????????????????>>>>"+photoUrl)
            var Mydb:MySqlHelper = MySqlHelper(this)
            println("A=======================>"+Mydb.readAllPerson().toString())
            val photo_url  = Mydb.readPerson(1.toString())
            var url =""
            if (!photo_url.isEmpty()){
                url =  photo_url.get(0).name.toString()
                Mydb.deletePerson(1.toString())
                println("~~~~~~~~~~~~~~~~~~~~~~~~~~>"+url)
            }
//            println("B=======================>"+photo_url)


            val MyStory: HashMap<String, String> = HashMap<String,String>()
            MyStory.put("title",txtTitle.text.toString())
            MyStory.put("story",txtStory.text.toString())
            MyStory.put("lat",lat)
            MyStory.put("lng",lng)
            MyStory.put("url",url)
            db.collection("Stories").document().set(MyStory as Map<String, Any>)
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
                imagePath = destination!!.getAbsolutePath()
                val storageRef = storage.reference
                val stream = FileInputStream(File(imagePath))
                val picRef = storageRef.child(dateToString(Date(), "yyyy-MM-dd-hh-mm-ss"))

               val  uploadTask = picRef.putStream(stream)
                uploadTask.addOnFailureListener { exception ->

                    println("Failed")
                }.addOnSuccessListener { taskSnapshot ->

                    println("OK")
                    picRef.downloadUrl.addOnCompleteListener () {taskSnapshot ->



                        photoUrl = taskSnapshot.result.toString()


                        var db:MySqlHelper = MySqlHelper(this)

                        val person = Person(1, photoUrl.toString(), "F", 38)
                        db.insert(person)

                        println ("url =" + photoUrl.toString ())

                    }
                }


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





