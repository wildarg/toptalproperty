package com.homesoftwaretools.toptalproperty.features.dashboard.map

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Location
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListViewModel
import org.koin.android.ext.android.inject
import kotlin.math.max
import kotlin.math.min

class ApartmentMapScreen : BaseFragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var map: GoogleMap? = null

    override val layoutId = R.layout.apartment_map_screen

    private val rp: ResourceProvider by inject()
    private val fmt: NumberFormatter by inject()
    private val vm: ApartmentListViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        mapView?.onCreate(null)
        mapView?.getMapAsync(this)
        vm.data.onChange(this::onDataChange)
    }

    private fun initView(v: View) {
        mapView = v.findViewById(R.id.map_view)
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onMapReady(p: GoogleMap?) {
        map = p
        map?.uiSettings?.apply {
            isCompassEnabled = true
            isMapToolbarEnabled = true
            isScrollGesturesEnabled = true
            isZoomGesturesEnabled = true
            isZoomControlsEnabled = true
        }
        map?.setInfoWindowAdapter(MarkerInfoAdapter(activity!!))
        vm.data.value?.let(this::onDataChange)
    }

    private fun onDataChange(list: List<Pair<Apartment, User>>) {
        map ?: return
        markers.values.forEach { it.remove() }
        markers.clear()
        val bounds = MapBounds()
        list.forEach { (ap, user) ->
            bounds.addPosition(ap.location.toLatLng())
            addMarker(ap, user)
        }
        map?.setLatLngBoundsForCameraTarget(bounds.getBounds())
    }

    private fun getSnippet(apartment: Apartment, user: User): String {
        return rp.string(R.string.marker_snippet_format_string).format(
            user.name,
            apartment.description,
            fmt.formatNum(apartment.area),
            fmt.formatInt(apartment.rooms),
            fmt.formatNum(apartment.price)
        ).let { if (apartment.rented == true) rp.string(R.string.rented_label) + "\n" + it else it }
    }

    private val markers = HashMap<Apartment, Marker>()

    private fun addMarker(apartment: Apartment, user: User) {
        val marker = MarkerOptions()
            .position(apartment.location.toLatLng())
            .title(apartment.name)
            .snippet(getSnippet(apartment, user))
            .icon(BitmapDescriptorFactory.fromResource(
                if (apartment.rented == true) R.drawable.ic_rented_house_24px else R.drawable.ic_house_24px
            ))
        map?.addMarker(marker)?.let { markers[apartment] = it }
    }

    private fun Location.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)

}

class MapBounds {

    private var minPos: LatLng? = null
    private var maxPos: LatLng? = null

    fun addPosition(pos: LatLng) {
        minPos = if (minPos == null) pos
        else LatLng(min(minPos!!.latitude, pos.latitude), min(minPos!!.longitude, pos.longitude))
        maxPos = if (maxPos == null) pos
        else LatLng(max(minPos!!.latitude, pos.latitude), max(minPos!!.longitude, pos.longitude))
    }

    fun getBounds(): LatLngBounds {
        return LatLngBounds(minPos, maxPos)
    }

}