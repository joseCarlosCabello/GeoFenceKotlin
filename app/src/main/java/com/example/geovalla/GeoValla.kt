package com.example.geovalla

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import com.google.android.gms.maps.model.LatLng


class GeoValla(base: Context?) : ContextWrapper(base) {
    var pendingIntent: PendingIntent? = null

    /*fun getGeoVallaRequest(geoValla: GeoValla?): GeoVallaRequest? {
        return null
    }

    fun getValla(ID: String?, larlng: LatLng?, radius: Float, transisionTrypes: Int): Geo? {
        return null
    }
*/
    companion object {
        private const val TAG = "GeoValla"
    }
}