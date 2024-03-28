package com.getir.patika.foodcouriers.map

import androidx.fragment.app.Fragment
import com.getir.patika.foodcouriers.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback

class MapInitializer(private val fragment: Fragment, private val callback: OnMapReadyCallback) {

    fun initializeMap() {
        val mapFragment = fragment.childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(callback)
    }
}
