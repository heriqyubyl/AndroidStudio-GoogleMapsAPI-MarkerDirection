package com.example.maps_app

import android.content.Context.*
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation : LatLng
    private lateinit var currentAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.uiSettings.isMapToolbarEnabled=true
        mMap = googleMap
        var locationList = mutableMapOf<String, Double>("latLoc1" to 0.0, "lngLoc1" to 0.0,"latLoc2" to 0.0, "lngLoc2" to 0.0 )
        var addressList = mutableMapOf<String, String>("address1" to "", "address2" to "")
        val Bekasi = LatLng(-6.238270, 106.975571)
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Bekasi, 16f))
        mMap.setOnMapClickListener {
            currentLocation = mMap.cameraPosition.target
            val geocoder = Geocoder(this)
            mMap.clear()
            var geocoderResult = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude,1)
            currentAddress = geocoderResult[0].getAddressLine(0)
            mMap.addMarker(MarkerOptions().position(currentLocation).title("Position"))
        }

        add.setOnClickListener{
        if (locationList.get("latLoc1") == 0.0){
            locationList.put("latLoc1", currentLocation.latitude)
            locationList.put("lngLoc1", currentLocation.longitude)
            addressList.put("address1", currentAddress)
        }else{
            locationList.put("latLoc2", currentLocation.latitude)
            locationList.put("lngLoc2", currentLocation.longitude)
            addressList.put("address2", currentAddress)
        }
        tv1_lat.text = "${locationList.get("latLoc1")}-${locationList.get("lngLoc1")}"
        tv1_long.text = addressList.get("address1").toString()

        tv2_lat.text ="${locationList.get("latLoc2")}-${locationList.get("lngLoc2")}"
        tv2_long.text =addressList.get("address2").toString()
    }
        Hitung.setOnClickListener{
            val loc1 = LatLng(locationList.get("latLoc1")!!, locationList.get("lngLoc2")!!)
            val location1 = Location("")
            location1.latitude = locationList.get("latLoc1")!!
            location1.longitude = locationList.get("lngLoc1")!!

            val loc2 = LatLng(locationList.get("latLoc1")!!, locationList.get("lngLoc2")!!)
            val location2 = Location("")
            location2.latitude = locationList.get("latLoc2")!!
            location2.longitude = locationList.get("lngLoc2")!!

            mMap.addMarker(MarkerOptions().position(loc1).title("position"))
            mMap.addMarker(MarkerOptions().position(loc2).title("position"))

            mMap.addPolyline(PolylineOptions()
                .clickable(true)
                .add(loc1,loc2)
                .color(R.color.red)
                .width(16f))
            val distance1 = location1.distanceTo(location2)
            tv_Hasil.text = "${distance1/1000} KM"
        }
        reset.setOnClickListener{
            locationList.putAll(setOf("latLoc1" to 0.0, "lngLoc1" to 0.0,"latLoc2" to 0.0, "lngLoc2" to 0.0))
            tv1_lat.text = ""
            tv1_long.text= ""
            tv2_lat.text = ""
            tv2_long.text= ""
            mMap.clear()
        }
    }
}