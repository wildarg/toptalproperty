package com.homesoftwaretools.toptalproperty.repo.firebase

import com.google.firebase.firestore.GeoPoint
import com.homesoftwaretools.toptalproperty.core.utils.DateProvider
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Location

interface ApartmentMapper {
    fun fromMap(map: Map<String?, Any?>?): Apartment?
    fun toMap(apartment: Apartment): Map<String, Any>
}

class FireStoreApartmentMapper(
    private val dt: DateProvider
) : ApartmentMapper {

    private fun getLocation(point: GeoPoint?): Location = Location(
        latitude = point?.latitude ?: 0.0,
        longitude = point?.longitude ?: 0.0
    )

    private fun getGeoPoint(location: Location): GeoPoint = GeoPoint(
        location.latitude, location.longitude
    )

    override fun fromMap(map: Map<String?, Any?>?): Apartment? = map?.let { data ->
        Apartment(
            id = data[FireStoreApartmentDao.ID] as? String,
            created = dt.fromMs(data[FireStoreApartmentDao.CREATED] as? Long) ?: dt.now(),
            realtorId = data[FireStoreApartmentDao.REALTOR_ID] as? String ?: "",
            name = data[FireStoreApartmentDao.NAME] as? String ?: "",
            description = data[FireStoreApartmentDao.DESCRIPTION] as? String ?: "",
            area = (data[FireStoreApartmentDao.AREA] as? Number)?.toDouble() ?: 0.0,
            price = (data[FireStoreApartmentDao.PRICE] as? Number)?.toDouble() ?: 0.0,
            rooms = (data[FireStoreApartmentDao.ROOMS] as? Number)?.toInt() ?: 0,
            location = getLocation(data[FireStoreApartmentDao.LOCATION] as? GeoPoint),
            rented = data[FireStoreApartmentDao.RENTED] as? Boolean ?: false
        )
    }

    override fun toMap(apartment: Apartment): Map<String, Any> {
        val map = hashMapOf(
            FireStoreApartmentDao.NAME to apartment.name,
            FireStoreApartmentDao.DESCRIPTION to apartment.description,
            FireStoreApartmentDao.AREA to apartment.area,
            FireStoreApartmentDao.PRICE to apartment.price,
            FireStoreApartmentDao.ROOMS to apartment.rooms,
            FireStoreApartmentDao.LOCATION to getGeoPoint(apartment.location)
        )

        if (apartment.created != null) map[FireStoreApartmentDao.CREATED] = dt.toMs(apartment.created)
        if (apartment.realtorId != null) map[FireStoreApartmentDao.REALTOR_ID] = apartment.realtorId
        if (apartment.rented != null) map[FireStoreApartmentDao.RENTED] = apartment.rented
        return map
    }

}