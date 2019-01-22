package com.example.spartakos87.story_map

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    var username: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA, Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions,0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val intent = intent
        username = intent!!.getStringExtra("username")
        println("SEREPAS===>"+username)


//        Add listener for float button
        val mFab = findViewById<FloatingActionButton>(R.id.FButton)
        mFab.setOnClickListener {
            val intent = Intent(this, Reports::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

        /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
        fun ZoomInMap(lat:Double, lng:Double){
            val temp = LatLng(lat,lng)
            mMap.addMarker(MarkerOptions().position(temp).title("You are here"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp,16.0f))
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
// https://stackoverflow.com/questions/19076124/android-map-marker-color

        //edo pairnei ola ta documents toy collection
        var coordinates:LatLng
        var temp_lat:Double
        var temp_lng:Double
        var color : Float
        val db: FirebaseFirestore
        db = FirebaseFirestore.getInstance()
        db.collection("Stories")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {

                            if (document.get("lat") == null || document.get("lng") == null){
                                println("Is nul")
                            }else
                           {
//                               Check if there is type and if select color
                               if (document.get("type") == null){
//                                   if there isnt such a field is red
                                    color = BitmapDescriptorFactory.HUE_RED
                               } else {
                                   when(document.get("type")){
                                       "Φωτισμός" -> color = BitmapDescriptorFactory.HUE_YELLOW
                                       "Απορρήματα" -> color = BitmapDescriptorFactory.HUE_BLUE
                                       "Αλλο" -> color = BitmapDescriptorFactory.HUE_ORANGE
                                       else -> color=BitmapDescriptorFactory.HUE_RED

                                   }
                               }
                               coordinates = LatLng(document.getString("lat").toDouble(), document.getString("lng").toDouble())
                               mMap.addMarker(MarkerOptions().position(coordinates)
                                       .icon(BitmapDescriptorFactory
                                               .defaultMarker(color))
                                       .title(document.getString("title")))
                                       .setTag(document.id)



                           }

                        }
                    } else {
                        Log.d("INFO", "Error getting documents: ", task.exception)
                    }
                }




        val tracker = LocationTracker(this@MapsActivity)
        if (tracker.isLocationEnabled) {
            val latitude = tracker.getLatitude()
            val longitude = tracker.getLongitude()
            ZoomInMap(latitude, longitude)

        } else {
            // show dialog box to user to enable location
            tracker.askToOnLocation()

        }
//        add marker after long pressure
        mMap.setOnMapLongClickListener {

            latLng ->
            val intent = Intent(this, AddYourStory::class.java)
            intent.putExtra("lat", latLng.latitude.toString())
            intent.putExtra("lng", latLng.longitude.toString())
            intent.putExtra("username", username)
            startActivity(intent)

        }



        mMap.setOnMarkerClickListener {marker ->
            val infowindow = Intent(this, InfoWindow::class.java)
            infowindow.putExtra("id",marker.tag.toString())
//            if the username is the same with the username of the creator of this marker you can delete
//            otherwise user cannot
            infowindow.putExtra("username",username)
            startActivity(infowindow)
            false
        }


    }


}
