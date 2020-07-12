package com.homesoftwaretools.toptalproperty.repo

import com.homesoftwaretools.toptalproperty.core.utils.DateProvider
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.Filter
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.repo.dao.ApartmentDao
import io.reactivex.Observable
import io.reactivex.Single

interface ApartmentRepo {
    fun save(apartment: Apartment, realtor: User): Single<Apartment>
    fun getAll(filter: Filter = Filter.EMPTY): Observable<List<Apartment>>
    fun get(id: String): Single<Apartment>
}


class ApartmentRepoImpl(
    private val dao: ApartmentDao,
    private val dt: DateProvider
) : ApartmentRepo {

    override fun save(apartment: Apartment, realtor: User): Single<Apartment> {
        return apartment
            .let { if (it.created == null && it.id == null) it.copy(created = dt.now()) else it }
            .let {
                if (it.realtorId != null && it.realtorId != realtor.authId)
                    return Single.error(Exception("Wrong user"))
                if (it.realtorId == null) it.copy(realtorId = realtor.authId) else it
            }
            .let(dao::save)
    }

    override fun getAll(filter: Filter): Observable<List<Apartment>> =
        dao.getAll(filter).map { list -> list.sortedBy { it.created } }

    override fun get(id: String): Single<Apartment> = dao.get(id)
}