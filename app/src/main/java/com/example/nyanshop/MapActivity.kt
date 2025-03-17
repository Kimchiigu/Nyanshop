package com.example.nyanshop

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.nyanshop.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var currLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // UI elements
    private lateinit var tvShopName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvItemName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnConfirm: Button

    // Data from intent
    private var itemName: String = ""
    private var itemPrice: String = ""
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI elements
        tvShopName = findViewById(R.id.tv_shop_name)
        tvLocation = findViewById(R.id.tv_location)
        tvItemName = findViewById(R.id.tv_item_name)
        tvPrice = findViewById(R.id.tv_price)
        btnConfirm = findViewById(R.id.btn_confirm)

        // Get data from intent
        itemName = intent.getStringExtra("ITEM_NAME") ?: "Unknown Pet"
        itemPrice = intent.getStringExtra("ITEM_PRICE") ?: "$0"
        itemId = intent.getIntExtra("ITEM_ID", -1)

        // Set data to UI
        tvItemName.text = itemName
        tvPrice.text = itemPrice

        // Set up confirm button
        btnConfirm.setOnClickListener {
            // In a real app, you would make an API call to purchase the pet
            Toast.makeText(this, "Purchase confirmed! Enjoy your new pet!", Toast.LENGTH_SHORT).show()

            // Create intent to go back to HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear the back stack
            startActivity(intent)
            finish()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrLocation()
    }

    private fun getCurrLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 102)
            return
        }

        val task: Task<Location> = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("MAP_ERROR", "location not null")
                currLocation = location

                // Update location text with coordinates
                tvLocation.text = "Lat: ${location.latitude.toFloat()}, Long: ${location.longitude.toFloat()}"
            } else {
                Log.d("MAP_ERROR", "location null")
                // Fake location (BINUS University)
                currLocation = Location("").apply {
                    longitude = 106.78113
                    latitude = -6.20201
                }

                // Set default location text
                tvLocation.text = "BINUS University, Jakarta"
            }

            // Load map **only after** we get the location
            mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 102) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrLocation()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        Log.d("MAP_ERROR", "ON MAP READY")
        val location = LatLng(currLocation.latitude, currLocation.longitude)
        val cameraPosition = CameraPosition.builder().target(location).zoom(15.0F).tilt(20.0F).build()

        // Add marker with shop name and item being purchased
        map.addMarker(MarkerOptions()
            .position(location)
            .title("Nyanshop Pet Store")
            .snippet("Pick up your $itemName here!"))

        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}

