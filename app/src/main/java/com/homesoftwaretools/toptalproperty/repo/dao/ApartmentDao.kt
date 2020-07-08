package com.homesoftwaretools.toptalproperty.repo.dao

import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Filter
import io.reactivex.Single

interface ApartmentDao {
    fun getAll(filter: Filter = Filter.EMPTY): Single<List<Apartment>>
    fun save(apartment: Apartment): Single<Apartment>
    fun delete(apartment: Apartment): Single<Apartment>
}

class ApartmentNotFoundException(id: String) : Exception("Apartment id: $id not found")