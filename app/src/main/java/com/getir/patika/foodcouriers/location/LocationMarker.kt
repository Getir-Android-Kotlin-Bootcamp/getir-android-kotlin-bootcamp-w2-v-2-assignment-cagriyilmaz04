package com.getir.patika.foodcouriers.location

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationMarker(private val context: Context) {

    fun addMarker(map: GoogleMap, location: LatLng, title: String, vectorResId: Int) {
        val markerOptions = MarkerOptions().position(location).title(title).icon(bitmapDescriptorFromVector(vectorResId))
        map.addMarker(markerOptions)
    }

    fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        return getBitmapDescriptorFromDrawable(drawable)
    }

    private fun getBitmapDescriptorFromDrawable(drawable: Drawable): BitmapDescriptor? {
        val wrappedDrawable = DrawableCompat.wrap(drawable).mutate()
        val bitmap = Bitmap.createBitmap(wrappedDrawable.intrinsicWidth, wrappedDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        wrappedDrawable.setBounds(0, 0, canvas.width, canvas.height)
        wrappedDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
