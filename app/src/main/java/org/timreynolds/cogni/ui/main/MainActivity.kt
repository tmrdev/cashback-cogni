package org.timreynolds.cogni.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.google.maps.android.ui.IconGenerator.STYLE_GREEN
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.timreynolds.cogni.MyApplication
import org.timreynolds.cogni.R
import org.timreynolds.cogni.data.Venues
import org.timreynolds.cogni.ui.base.BaseActivity
import org.timreynolds.cogni.ui.login.LoginActivity




/**
 * MainActivity
 */
class MainActivity : BaseActivity(), MainMvpView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mainPresenter: MainPresenter<MainMvpView>
    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        mainPresenter = MainPresenter((application as MyApplication).dataManager)
        mainPresenter.onAttach(this)
        mainPresenter.getVenues()

        // set left nav textviews
        var myNavHeader = nav_view.getHeaderView(0)
        myNavHeader.findViewById<TextView>(R.id.navUsernameTitle).setText(mainPresenter.getName())
        myNavHeader.findViewById<TextView>(R.id.navEmailTitle).setText(mainPresenter.getEmail())
        // fused location will update the current location for the user on the map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // set google map, requires API key
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_drawer) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
                // Set current location
                val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                if(mMap != null && mMap.isMyLocationEnabled) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10.0f))
                } else {
                    Log.i(TAG, "")
                }
            }
        }
        createLocationRequest()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_venues -> {
                // leaving this call in to re-test get venues api call
                // normally link would reload entire activity if coming from another area
                mainPresenter.getVenues()
            }
            R.id.nav_logout -> {
                mainPresenter.logout()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    /**
     * startActivity - start activity back up, especially when user accepts location permission
     */
    fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun gotoLoginActivity() {
        val intent = LoginActivity.getNewIntent(applicationContext)
        startActivity(intent)
        finish()
    }

    /**
     * displayVenues - API data for all venues gets passed into this method and custom marker information is then displayed
     */
    override fun displayVenues(myVenues: List<Venues>?) {
        for(item in myVenues!!) {
            if(mMap.isMyLocationEnabled) {
                val venueLocation = LatLng(item.lat, item.long)

                var iconGen = IconGenerator(this)
                iconGen.setTextAppearance(R.style.myMarkerStyleText);
                iconGen.setColor(R.color.marker_background)
                //iconGen.setStyle(R.style.myMarkerStyle)
                iconGen.setStyle(STYLE_GREEN)
                //iconGen.setBackground(getResources().getDrawable(R.drawable.marker_shape));
                var markerOptions = MarkerOptions().icon(BitmapDescriptorFactory
                        .fromBitmap(iconGen.makeIcon(item.cashback.toString() + "%")))
                        .position(venueLocation)
                        .anchor(iconGen.getAnchorU(), iconGen.getAnchorV())


//                val markerOptions = MarkerOptions()
//                markerOptions.position(venueLocation)
//                        .title(item.name)
//                        .snippet(item.cashback.toString())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.googlemapsicongreenhi))

                val info = MapWindowData(item.name, item.city, item.cashback.toString())
                val customInfoWindow = CustomInfoWindowGoogleMap(this)
                mMap.setInfoWindowAdapter(customInfoWindow)
                val marker = mMap.addMarker(markerOptions)
                marker.tag = info
                //marker.showInfoWindow()
            }
        }
    }

    /**
     * onMapReady - method that sets up google maps and defines access to the map in the Activity class. all methods will access this map variable
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setupMap()
    }

    fun setupMap() {
        // make sure user has accepted permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
    }

    /**
     * startLocationUpdates - make sure devies above API 23 have accepted location permissions
     */
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        // using custom info window setup
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // update interval for user location
        locationRequest.interval = 20000
        locationRequest.fastestInterval = 15000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Show user dialog if location settings are not enabled
                try {
                    // show dialog and check onActivityResult
                    e.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Trap here if needed
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     startActivity()
                } else {
                    showMessage("Location can not be found without permission")
                    //Toast.makeText(this, "Cannot get Location Updates", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    data class MapWindowData(val mVenueTitle: String,
                             val mCityTitle: String,
                             val mCashbackValue: String)

    class CustomInfoWindowGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {

        override fun getInfoContents(p0: Marker?): View {
            var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.marker_layout, null)
            var mMapWindow: MapWindowData? = p0?.tag as MapWindowData?
            mInfoView.findViewById<TextView>(R.id.venueTitle).setText(mMapWindow?.mVenueTitle?.capitalize())
            mInfoView.findViewById<TextView>(R.id.cityName).setText(mMapWindow?.mCityTitle?.capitalize())
            mInfoView.findViewById<TextView>(R.id.cashbackValue).setText("Cash Back: " + mMapWindow?.mCashbackValue + "%")
            return mInfoView
        }

        override fun getInfoWindow(p0: Marker?): View? {
            return null
        }
    }
}