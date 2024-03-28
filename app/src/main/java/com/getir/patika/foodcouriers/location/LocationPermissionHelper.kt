package com.getir.patika.foodcouriers.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class LocationPermissionHelper(private val fragment: Fragment) {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission() {
        fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }
}
