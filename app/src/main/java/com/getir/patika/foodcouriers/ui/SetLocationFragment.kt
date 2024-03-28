package com.getir.patika.foodcouriers.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.getir.patika.foodcouriers.R
import com.getir.patika.foodcouriers.databinding.FragmentSetLocationBinding
import com.getir.patika.foodcouriers.location.LocationMarker
import com.getir.patika.foodcouriers.location.LocationPermissionHelper
import com.getir.patika.foodcouriers.map.MapInitializer
import com.getir.patika.foodcouriers.places.PlacesAutocompleteUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import java.io.IOException
import java.util.Locale


class SetLocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSetLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var autocompleteResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var mapInitializer: MapInitializer
    private lateinit var placesAutocompleteUtil: PlacesAutocompleteUtil
    private lateinit var locationMarker: LocationMarker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSetLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize helpers and utilities
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationPermissionHelper = LocationPermissionHelper(this)
        mapInitializer = MapInitializer(this, this)
        placesAutocompleteUtil = PlacesAutocompleteUtil(requireContext())
        locationMarker = LocationMarker(requireContext())
        autocompleteResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                val place = Autocomplete.getPlaceFromIntent(result.data!!)
                place.latLng?.let {
                    mMap.clear()
                    locationMarker.addMarker(mMap, it, place.name ?: "Selected Location",
                        R.drawable.pin_radar
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 14f))
                    binding.textViewLocation.text = place.address
                }
            }
        }

        binding.constraintSearch.setOnClickListener {
            placesAutocompleteUtil.launchAutocompleteIntent(autocompleteResultLauncher)
        }

        checkLocationPermissionAndInitializeMap()
    }

    private fun checkLocationPermissionAndInitializeMap() {
        if (!locationPermissionHelper.hasLocationPermission()) {
            locationPermissionHelper.requestLocationPermission()
        } else {
            mapInitializer.initializeMap()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = true
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        if (!locationPermissionHelper.hasLocationPermission()) return
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                locationMarker.addMarker(mMap, userLatLng, "Your Location", R.drawable.pin_radar)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14f))

                // Kullanıcının mevcut konumunun adresini al ve textViewLocation'a yaz
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (addresses!!.isNotEmpty()) {
                        val address = addresses[0].getAddressLine(0) // Adres bilgisini al
                        binding.textViewLocation.text = address // Adres bilgisini textView'a yaz
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Adres alınırken hata oluştu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationPermissionHelper.LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mapInitializer.initializeMap()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
