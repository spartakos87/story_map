package com.example.spartakos87.story_map

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions




class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location





    override fun onCreate(savedInstanceState: Bundle?) {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA,android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

            val tracker = LocationTracker(this@MapsActivity)
            if(tracker.isLocationEnabled) {
                val latitude = tracker.getLatitude()
                val longitude = tracker.getLongitude()
                ZoomInMap(latitude,longitude)

            }
            else
            {
                // show dialog box to user to enable location
                tracker.askToOnLocation()
            }
//        add marker after long pressure
        mMap.setOnMapLongClickListener {

            latLng ->
            val intent = Intent(this,AddYourStory::class.java)
intent.putExtra("lat",latLng.latitude.toString())
            intent.putExtra("lng",latLng.longitude.toString())
            startActivity(intent)

//            googleMap.addMarker(MarkerOptions()
//                    .position(latLng)
//                    .title("Your marker title")
//                    .snippet("Your marker snippet")

//            )
        }
    }



}
