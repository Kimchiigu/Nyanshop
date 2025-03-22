package com.example.nyanshop

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.nyanshop.database.DatabaseHelper
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
    private lateinit var db: DatabaseHelper

    private lateinit var tvShopName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvItemName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnConfirm: Button

    private var itemName: String = ""
    private var itemPrice: String = ""
    private var petId: Int = -1
    private var storeId: String = ""
    private var storeName: String = "Unknown Store"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        tvShopName = findViewById(R.id.tv_shop_name)
        tvLocation = findViewById(R.id.tv_location)
        tvItemName = findViewById(R.id.tv_item_name)
        tvPrice = findViewById(R.id.tv_price)
        btnConfirm = findViewById(R.id.btn_confirm)

        itemName = intent.getStringExtra("ITEM_NAME") ?: "Unknown Pet"
        itemPrice = intent.getStringExtra("ITEM_PRICE") ?: "$0"
        petId = intent.getIntExtra("PET_ID", -1)
        storeId = intent.getStringExtra("STORE_ID") ?: "Unknown Store"
        storeName = intent.getStringExtra("STORE_NAME") ?: "Unknown Store"

        tvItemName.text = itemName
        tvPrice.text = "$$itemPrice"
        tvShopName.text = storeName

        btnConfirm.setOnClickListener {
            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
            val userEmail = sharedPref.getString("email", null)

            if (userEmail != null) {
                val isUpdated = db.updateUserPet(userEmail, petId)

                if (isUpdated) {
                    Toast.makeText(this, "Purchase confirmed! Enjoy your new pet!", Toast.LENGTH_SHORT).show()

                    with(sharedPref.edit()) {
                        putInt("item_id", petId)
                        apply()
                    }
                } else {
                    Toast.makeText(this, "Failed to update user!", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
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
                tvLocation.text = "Unknown"
            } else {
                Log.d("MAP_ERROR", "location null, using default location")
                currLocation = Location("").apply {
                    longitude = 106.78113
                    latitude = -6.20201
                }
                tvLocation.text = "BINUS University, Jakarta"
            }

            // Fetch the store's latitude and longitude from the database
            val store = db.getStore(storeId)
            store?.let {
                val storeLocation = LatLng(it.latitude, it.longitude) // Use latitude and longitude from database
                tvLocation.text = store.storeLocation
                mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
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

        // Use the store's latitude and longitude for the marker
        val store = db.getStore(storeId)
        store?.let {
            val location = LatLng(it.latitude, it.longitude)  // Fetch latitude and longitude from the store
            val cameraPosition = CameraPosition.builder().target(location).zoom(15.0F).tilt(20.0F).build()

            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Nyanshop Pet Store - $storeName")
                    .snippet("Pick up your $itemName here!")
            )

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}

