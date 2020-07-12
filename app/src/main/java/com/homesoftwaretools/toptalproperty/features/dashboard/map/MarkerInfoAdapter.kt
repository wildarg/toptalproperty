package com.homesoftwaretools.toptalproperty.features.dashboard.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.visible
import com.homesoftwaretools.toptalproperty.domain.Apartment

class MarkerInfoAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    private val inflater = LayoutInflater.from(context)
    private val infoWindow: View = inflater.inflate(R.layout.info_window, null)

    override fun getInfoContents(marker: Marker?): View? {
        return marker?.let { populate(infoWindow, it) }
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return marker?.let { populate(infoWindow, it) }
    }

    private fun populate(v: View, m: Marker): View {
        v.findViewById<TextView>(R.id.title).text = m.title
        v.findViewById<TextView>(R.id.snippet).text = m.snippet
        (m.tag as? Apartment)?.let { ap ->
            v.findViewById<View>(R.id.rented_band)?.visible = ap.rented ?: false
        }
        return v
    }

}