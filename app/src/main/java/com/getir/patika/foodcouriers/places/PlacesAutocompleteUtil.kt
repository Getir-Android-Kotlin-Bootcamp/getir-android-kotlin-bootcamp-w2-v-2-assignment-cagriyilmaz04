package com.getir.patika.foodcouriers.places

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.getir.patika.foodcouriers.R
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class PlacesAutocompleteUtil(private val context: Context) {

    init {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_maps_key))
        }
    }

    fun launchAutocompleteIntent(autocompleteResultLauncher: ActivityResultLauncher<Intent>) {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context)
        autocompleteResultLauncher.launch(intent)
    }
}
