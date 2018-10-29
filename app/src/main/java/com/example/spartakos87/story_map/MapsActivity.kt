package com.example.spartakos87.story_map

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location





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


        //edo pairnei ola ta documents toy collection
        var coordinates:LatLng
        var temp_lat:Double
        var temp_lng:Double
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
                               coordinates = LatLng(document.getString("lat").toDouble(), document.getString("lng").toDouble())
                               mMap.addMarker(MarkerOptions().position(coordinates).title(document.getString("title"))).setTag(document.id)


//            )
                           }
//                            Log.d("INFO",document.id + " => " + document.data)
                        }
                    } else {
                        Log.d("INFO", "Error getting documents: ", task.exception)
                        print("SEREPAS")
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
            startActivity(intent)

        }



        mMap.setOnMarkerClickListener {marker ->
            val infowindow = Intent(this, InfoWindow::class.java)
            infowindow.putExtra("id",marker.tag.toString())
            startActivity(infowindow)
            false
        }


    }


}
