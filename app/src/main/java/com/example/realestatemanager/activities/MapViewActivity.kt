package com.example.realestatemanager.activities
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.api.AddressRepository
import com.example.realestatemanager.BuildConfig.MAPS_API_KEY
import com.example.realestatemanager.viewModel.EstateViewModel
import com.example.realestatemanager.R
import com.example.realestatemanager.model.EstateData
import com.example.realestatemanager.viewModel.AddressesViewModel
import com.example.realestatemanager.viewModel.ConvertorFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
@Suppress("DEPRECATION")
class MapViewActivity:AppCompatActivity(),OnMapReadyCallback{
    private lateinit var addressesViewModel: AddressesViewModel
    private lateinit var estateViewModel: EstateViewModel
    private var mapView: GoogleMap? = null
    private lateinit var lastLocation: Location
    private lateinit var placesClient: PlacesClient
    private var cameraPosition: CameraPosition? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    companion object {
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        const val TAG = "myLocation"
        const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
    private lateinit var allEstates: List<EstateData>
    private val defaultLocation = LatLng(37.076526, 36.242001)
    private var locationPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        setContentView(R.layout.fragment_map_view)
        // Construct a PlacesClient
        Places.initialize(applicationContext, MAPS_API_KEY)
        placesClient = Places.createClient(this)
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        // Build the map.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        mapView?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastLocation)
        }
        super.onSaveInstanceState(outState)
    }
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        this.mapView = map
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.mapView?.setInfoWindowAdapter(@SuppressLint("PotentialBehaviorOverride")
        object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }
            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    findViewById<FrameLayout>(R.id.mapView), false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })
        // Prompt the user for permission.
        getLocationPermission()
        getDeviceLocation()
        addressDetails()
    }
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastLocation = Location(LocationManager.NETWORK_PROVIDER)

                        lastLocation = task.result
                        mapView?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(lastLocation.latitude,
                                lastLocation.longitude), DEFAULT_ZOOM.toFloat()))
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mapView?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mapView?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
    /**
     * Handles the result of the request for location permissions.
     */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                   locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    // Displaying estate location and address in a map marked with pin
    @SuppressLint("SuspiciousIndentation")
    private fun addressDetails(){
        val apiKey=MAPS_API_KEY
        val repository= AddressRepository()
        val convertorFactory= ConvertorFactory(repository)
        addressesViewModel= AddressesViewModel(repository)
        addressesViewModel= ViewModelProvider(this,convertorFactory)[AddressesViewModel::class.java]
        estateViewModel= ViewModelProvider(this)[EstateViewModel::class.java]
        estateViewModel.readAllData.observe(({ lifecycle })){estates->
        addressesViewModel.getAddressesDetails(estates[0].address,apiKey)
        addressesViewModel.addressesResponse.observe({ lifecycle }){response->
            if (response.isSuccessful){
                response.body().let { addressResponse->
                    for (i in 0 until addressResponse!!.results.size){
                        addressResponse.results[i].address_components
                        val location=LatLng(addressResponse.results[i].geometry.location.lat,addressResponse.results[i].geometry.location.lng)
                            mapView?.clear()
                            allEstates = estates
                            for (estate in estates) {
                                    val markerOptions = MarkerOptions()
                                        .position(location)
                                        .title(estate.type)
                                        .snippet(estate.address)
                                    val marker = mapView?.addMarker(markerOptions)
                                    marker?.tag = estates[i]
                            }
                        }
                    mapView?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { Marker ->
                        val gson = Gson()
                        val myTag = gson.toJson(Marker.tag)
                            val intent =  Intent(this, DescriptionDetailsFromMapActivity::class.java)
                            intent.putExtra("id", Marker.id)
                            intent.putExtra("markerTag", myTag)
                        startActivity(intent)
                            return@OnMarkerClickListener false
                        })
                    }
                }
            }
        }
    }
}