package com.example.geovalla

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.geovalla.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location


    //
    private val channelID= "channelID"
    private val channelName= "channelName"
    private val notificationID =0

    //

/*


    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
    val builder = NotificationCompat.Builder(this, "not")
    .setSmallIcon(R.drawable.googleg_disabled_color_18)
    .setContentTitle("My notification")
    .setContentText("Hello World!")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    // Set the intent that will fire when the user taps the notification
    .setContentIntent(pendingIntent)
    .setAutoCancel(true)

    */



    //

    lateinit var geofencingClient: GeofencingClient


    //

    val latLng = LatLng(20.687610,-103.323364)

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE =1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                //
        geofencingClient = LocationServices.getGeofencingClient(this)
        //

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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
        mMap = googleMap

        //
        val notification = NotificationCompat.Builder(this,channelID).also {
            it.setContentTitle("hola")
            it.setContentText("mundo")
            it.setSmallIcon(R.drawable.ic_message)
            it.setPriority(NotificationCompat.PRIORITY_HIGH)

        }.build()


        val notificationManager = NotificationManagerCompat.from(this)


        //

       /* // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/


        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled=true
        setUpMap()
        addCircle(mMap)
        //
        val geo = Geofence.Builder()
        geo.setRequestId("1")

        geo.setCircularRegion(20.687610,-103.323364, 1000F)
        geo.setExpirationDuration(Geofence.NEVER_EXPIRE)
        geo.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        geo.build()
        if(Geofence.GEOFENCE_TRANSITION_ENTER==1)
        {
            notificationManager.notify(notificationID,notification)
        }
       /* with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }*/


       /* geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added
                // ...
            }
            addOnFailureListener {
                // Failed to add geofences
                // ...
            }
        }*/

        //

    }

   /* private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
            }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {location->
            if(location!=null){
                lastLocation=location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))
        }
        }*/

        private fun setUpMap(){
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                return
            }
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) {location->

                if(location!=null){
                    lastLocation=location
                    // val currentLatLong = LatLng(location.latitude, location.longitude)
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 13f))

                }


            }
        }



    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
        false
    }


    private var circle: Circle? = null
    private fun addCircle(googleMap: GoogleMap) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(1000.0)
                .fillColor(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light_disabled))
                .strokeColor(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light_disabled))
        )
    }


    //
    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        }.build()
    }

    /*

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, MyReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

    */

//crear canal
   private fun createNotificationChannel()
   {
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
       {
           val importance: Int = NotificationManager.IMPORTANCE_HIGH

           val channel=NotificationChannel(channelID,channelName,importance).apply {
               lightColor = Color.RED
               enableLights(true)
           }
           val manager: NotificationManager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

           manager.createNotificationChannel(channel)
       }


    }


    //
}
