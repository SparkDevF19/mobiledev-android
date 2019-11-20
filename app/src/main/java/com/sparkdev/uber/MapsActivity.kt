package com.sparkdev.uber

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // called when marker is clicked or tapped
    override fun onMarkerClick(p0: Marker?) = false

    // lateinit = used to declare a variable without initializing it in the constructor
    // map object
    private lateinit var map: GoogleMap

    // used to return most recent location
    private lateinit var lastLocation: Location

    // location callback = used for receiving notifications from the FusedLocationProviderApi
    // when the device location has changed or can no longer be determined
    private lateinit var locationCallback: LocationCallback

    // location request = used to request a quality of service for location updates from the FusedLocationProviderApi
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    // fused location client = location API in Google Play services that combines
    // different signals to provide the location information for your app
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mapFragment: SupportMapFragment

    // companion object = function/property that is tied to a class rather than to instances of it
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val searchView : SearchView = findViewById(R.id.sv_location)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // update lastLocation with the new location and update the map with the new location coordinates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
            }
        }
        createLocationRequest()

        mapFragment.getMapAsync(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                val location = searchView.query.toString()
                var addressList: List<Address>? = null

                if (location == null || location == "") {
                    Toast.makeText(
                        applicationContext,
                        "Please provide a location",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val geoCoder = Geocoder(this@MapsActivity)
                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // add location to list of addresses and place a marker on the location that the user typed in
                    var address: Address? = null
                    if (addressList != null && addressList.isNotEmpty()) {
                        address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        placeMarkerOnMap(latLng)
                        map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    }
                    return false
                }
                return true
            }
        })
        nav_button.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // enables my location layer, blue dot appears on user's current location
        // also adds button that centers on location
        map.isMyLocationEnabled = true

        // returns most recent location
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)

                /* move camera to zoom into current location
                               zoom levels in google maps (0 - 21)
                               1: world
                               5: landmass / continent
                               10: city
                               15: streets
                               20: buildings */
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    /**◊
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // allows you to zoom in to your location
        map.uiSettings.isZoomControlsEnabled = true

        // declare MapsActivity as the callback triggered when the user clicks a marker on this map
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // Create a MarkerOptions object and set the user’s current location as the position for the marker
        val markerOptions = MarkerOptions().position(location)
        // Add the marker to the map
        map.addMarker(markerOptions)
    }

    private fun startLocationUpdates() {
        // if the ACCESS_FINE_LOCATION permission has not been granted, request it now and return
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        // else request for location updates
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    private fun createLocationRequest() {
        // create an instance of LocationRequest,
        // add it to an instance of LocationSettingsRequest.Builder
        // and retrieve and handle any changes to be made based on the current state
        // of the user’s location settings
        locationRequest = LocationRequest()

        // interval = specifies the rate at which your app will receive updates
        locationRequest.interval = 10000

        // fastestInterval = specifies the fastest rate at which the app can handle updates
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // create a settings client and a task to check location settings
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // success = all is well and you can go ahead and initiate a location request
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // failure = location settings have some issues
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this@MapsActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    // Override AppCompatActivity’s onActivityResult() method and start the update request
    // if it has a RESULT_OK result for a REQUEST_CHECK_SETTINGS request

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    // Override onPause() to stop location update request
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Override onResume() to restart the location update request
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }
}








