package com.homesoftwaretools.toptalproperty.domain

import java.util.*

data class Location(val latitude: Double, val longitude: Double)

data class Filter(
    val minArea: Double? = null,
    val maxArea: Double? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minRooms: Int? = null,
    val maxRooms: Int? = null
) {
    companion object {
        val EMPTY = Filter()
    }
}

data class Apartment(
    val id: String? = null,
    val created: Date? = null,
    val realtorId: String? = null,
    val name: String,
    val description: String,
    val area: Double,
    val price: Double,
    val rooms: Int,
    val location: Location,
    val rented: Boolean? = null
)